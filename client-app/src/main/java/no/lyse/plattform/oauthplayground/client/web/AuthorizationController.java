package no.lyse.plattform.oauthplayground.client.web;

import jakarta.servlet.http.HttpServletRequest;
import no.lyse.plattform.oauthplayground.client.config.WebClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class AuthorizationController {
    private final WebClient webClient;
    private final String messagesBaseUri;

    public AuthorizationController(WebClient webClient,
                                   @Value("${messages.base-uri}") String messagesBaseUri) {
        this.webClient = webClient;
        this.messagesBaseUri = messagesBaseUri;
    }

    @GetMapping(value = "/authorize", params = "grant_type=authorization_code")
    public Mono<String> authorizationCodeGrant(Model model,
                                               @RegisteredOAuth2AuthorizedClient("messaging-client-authorization-code")
                                         OAuth2AuthorizedClient authorizedClient)
    {
        return messages(model, oauth2AuthorizedClient(authorizedClient));
    }

    // '/authorized' is the registered 'redirect_uri' for authorization_code
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
            .flatMap(l -> {
                model.addAttribute("messages", l.toArray(new String[0]));
                return Mono.just("index");
            });
    }

    @GetMapping(value = "/authorized", params = OAuth2ParameterNames.ERROR)
    public Mono<String> authorizationFailed(Model model, HttpServletRequest request) {
        String errorCode = request.getParameter(OAuth2ParameterNames.ERROR);
        if (StringUtils.hasText(errorCode)) {
            model.addAttribute("error",
                new OAuth2Error(
                    errorCode,
                    request.getParameter(OAuth2ParameterNames.ERROR_DESCRIPTION),
                    request.getParameter(OAuth2ParameterNames.ERROR_URI))
            );
        }

        return Mono.just("index");
    }
}
