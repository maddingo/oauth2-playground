package no.lyse.plattform.oauthplayground.client.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class AuthorizationController {
    private final WebClient webClient;
    private final String messagesBaseUri;

    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    public AuthorizationController(WebClient webClient,
                                   @Value("${messages.base-uri}") String messagesBaseUri, ReactiveOAuth2AuthorizedClientService authorizedClientService) {
        this.webClient = webClient;
        this.messagesBaseUri = messagesBaseUri;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping(value = "/authorize", params = "grant_type=authorization_code")
    public Mono<String> authorizationCodeGrant(Model model,
                                               @RegisteredOAuth2AuthorizedClient("messaging-client-authorization-code")
                                               OAuth2AuthorizedClient authorizedClient
    ) {
        return messages(model, oauth2AuthorizedClient(authorizedClient));
    }

    @GetMapping(value = "/authorize", params = "grant_type=client_credentials")
    public Mono<String> clientCredentialsGrant(Model model) {


        return messages(model, clientRegistrationId("messaging-client-client-credentials"));
    }

    private Mono<String> messages(Model model, Consumer<Map<String, Object>> clientAttribute) {
        return this.webClient
            .get()
            .uri(this.messagesBaseUri)
            .attributes(clientAttribute)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<String>>() {
            })
            .doOnSuccess(l -> model.addAttribute("messages", l.toArray(new String[0])))
            .thenReturn("index");
    }

    @GetMapping(value = "/authorize")
    public Mono<String> authorizationFailed(Model model, ServerWebExchange exchange) {
        MultiValueMap<String, String> params = exchange.getRequest().getQueryParams();
        String errorCode = params.getFirst(OAuth2ParameterNames.ERROR);
        if (StringUtils.hasText(errorCode)) {
            model.addAttribute("error",
                new OAuth2Error(
                    errorCode,
                    params.getFirst(OAuth2ParameterNames.ERROR_DESCRIPTION),
                    params.getFirst(OAuth2ParameterNames.ERROR_URI))
            );
        }

        return Mono.just("index");
    }
}
