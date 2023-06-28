package no.lyse.plattform.oauthplayground.client.web;

import jakarta.servlet.http.HttpServletRequest;
import no.lyse.plattform.oauthplayground.client.data.Joke;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class AuthorizationController {
    private final WebClient webClient;
    private final String messagesBaseUri;

    public AuthorizationController(WebClient webClient,
                                   @Value("${messages.base-uri}") String messagesBaseUri
    ) {
        this.webClient = webClient;
        this.messagesBaseUri = messagesBaseUri;
    }

    @GetMapping(value = "/authorize", params = "grant_type=authorization_code")
    public String authorizationCodeGrant(Model model,
                                         @RegisteredOAuth2AuthorizedClient("messaging-client-authorization-code")
                                         OAuth2AuthorizedClient authorizedClient) {

        addMessagesToModel(model, oauth2AuthorizedClient(authorizedClient));

        return "index";
    }

    // '/authorized' is the registered 'redirect_uri' for authorization_code
    @GetMapping(value = "/authorized", params = OAuth2ParameterNames.ERROR)
    public String authorizationFailed(Model model, HttpServletRequest request) {
        String errorCode = request.getParameter(OAuth2ParameterNames.ERROR);
        if (StringUtils.hasText(errorCode)) {
            model.addAttribute("error",
                new OAuth2Error(
                    errorCode,
                    request.getParameter(OAuth2ParameterNames.ERROR_DESCRIPTION),
                    request.getParameter(OAuth2ParameterNames.ERROR_URI))
            );
        }

        return "index";
    }

    @GetMapping(value = "/authorize", params = "grant_type=client_credentials")
    public String clientCredentialsGrant(Model model) {

        addMessagesToModel(model, clientRegistrationId("messaging-client-client-credentials"));

        return "index";
    }

    private void addMessagesToModel(Model model, Consumer<Map<String, Object>> clientAttribute) {
        String[] messages = this.webClient
            .get()
            .uri(UriComponentsBuilder.fromUriString(messagesBaseUri).path("/messages").build().toUri())
            .attributes(clientAttribute)
            .retrieve()
            .bodyToMono(String[].class)
            .block();
        model.addAttribute("messages", messages);

        Joke joke = this.webClient
            .get()
            .uri(UriComponentsBuilder.fromUriString(messagesBaseUri).path("/joke").build().toUri())
            .attributes(clientAttribute)
            .retrieve()
            .bodyToMono(Joke.class)
            .log()
            .block();
        model.addAttribute("joke", joke);
    }
}
