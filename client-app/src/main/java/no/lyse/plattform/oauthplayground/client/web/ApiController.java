package no.lyse.plattform.oauthplayground.client.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import no.lyse.plattform.oauth2playground.jokeapi.Joke;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    private final WebClient webClient;
    private final String messagesBaseUri;

    private final OAuth2ClientProperties clientProperties;

    public ApiController(
        WebClient webClient,
        ClientRegistrationRepository clientRegistrationRepository,
        @Value("${messages.base-uri}") String messagesBaseUri,
        OAuth2ClientProperties clientProperties) {
        this.webClient = webClient;
        this.messagesBaseUri = messagesBaseUri;
        this.clientProperties = clientProperties;
    }

    @GetMapping("/joke")
    public ResponseEntity<Joke> joke() {
        return getJoke(clientRegistrationId("messaging-client-client-credentials"));
    }

    @GetMapping("/joke1")
    public ResponseEntity<Joke> joke1(
        @RegisteredOAuth2AuthorizedClient("messaging-client-authorization-code")
        OAuth2AuthorizedClient authorizedClient
    ) {
        return getJoke(oauth2AuthorizedClient(authorizedClient));
    }

    @GetMapping("/auth/clients")
    public ResponseEntity<Collection<String>> clients() {
        return ResponseEntity.ok(clientProperties.getRegistration().keySet());
    }

    @GetMapping("/auth/session")
    public ResponseEntity<Map<String, Object>> session(NativeWebRequest webRequest, Principal principal) {
        return Optional.ofNullable(principal)
            .flatMap(p -> {
                    if (p instanceof OAuth2AuthenticationToken t) {
                        return Optional.of(t);
                    } else {
                        return Optional.empty();
                    }
                }
            )
            .map(token -> {
                String expires = Optional.ofNullable(webRequest.getNativeRequest(HttpServletRequest.class))
                    .map(r -> r.getSession(false))
                    .map(ApiController::formatSessionExpiration)
                    .orElse("");
                return Map.of(
                    "user",
                    Map.of(
                        "id", "1",
                        "name", token.getName(),
                        "email", token.getName() + "@test.no",
                        "image", "/pingu_hahn.jpg"
                    ),
                "expires", expires);
            })
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/api/auth/_log")
    public Map<String, String> log(@RequestBody String body) {
        log.info(body);
        return Map.of("status", "ok");
    }

    private static String formatSessionExpiration(HttpSession session) {
        ZonedDateTime expiryTime = Instant.ofEpochMilli(session.getLastAccessedTime() + session.getMaxInactiveInterval()).atOffset(ZoneOffset.UTC).toZonedDateTime();
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(expiryTime);
    }

    private ResponseEntity<Joke> getJoke(Consumer<Map<String, Object>> clientAttribute) {
        return ResponseEntity.ok(this.webClient
            .get()
            .uri(UriComponentsBuilder.fromUriString(messagesBaseUri).path("/joke").build().toUri())
            .attributes(clientAttribute)
            .retrieve()
            .bodyToMono(Joke.class)
            .log()
            .block());
    }

}
