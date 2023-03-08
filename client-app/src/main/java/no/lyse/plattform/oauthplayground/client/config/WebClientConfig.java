package no.lyse.plattform.oauthplayground.client.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Documentation and examples are a mix of reactive and non-reactive. Reactive is often outdated.
 *
 * See https://github.com/spring-projects/spring-security/issues/8444#issuecomment-621506498
 */
@Configuration(proxyBeanMethods = false)
public class WebClientConfig {
    @Bean
    public WebClient webClient(
        ReactiveClientRegistrationRepository clientRegistrationRepository,
        ServerOAuth2AuthorizedClientRepository authorizedClientRepository
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
            clientRegistrationRepository,
            authorizedClientRepository);
        WebClient client = WebClient.builder()
            .filter(oauth2Client)
            .build();
        return client;
    }

//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
//        ReactiveClientRegistrationRepository clientRegistrationRepository,
//        ServerOAuth2AuthorizedClientRepository authorizedClientRepository
//    ) {
//
//        ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider =
//            ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                .authorizationCode()
//                .refreshToken()
//                .clientCredentials()
//                .build();
//        DefaultReactiveOAuth2AuthorizedClientManager authorizedClientManager =
//          new DefaultReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
////        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
////            new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return new DelegatingReactiveOAuth2AuthorizedClientManager(authorizedClientManager);
//    }

//    @RequiredArgsConstructor
//    private static class DelegatingReactiveOAuth2AuthorizedClientManager implements ReactiveOAuth2AuthorizedClientManager {
//
//        private final DefaultReactiveOAuth2AuthorizedClientManager withSecurityContextManager;
//
//        @Override
//        public Mono<OAuth2AuthorizedClient> authorize(OAuth2AuthorizeRequest authorizeRequest) {
//            return withSecurityContextManager.authorize(authorizeRequest);
//        }
//    }
}
