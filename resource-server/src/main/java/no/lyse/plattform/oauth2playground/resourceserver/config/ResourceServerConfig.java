package no.lyse.plattform.oauth2playground.resourceserver.config;

import org.springframework.boot.actuate.autoconfigure.web.exchanges.HttpExchangesAutoConfiguration;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch;

@EnableWebFluxSecurity
@Configuration(proxyBeanMethods = false)
@Import(HttpExchangesAutoConfiguration.class)
public class ResourceServerConfig {
    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
        http
//            .securityMatcher(this::securityMatcher)
                .authorizeExchange()
                .pathMatchers("/messages/**")
                .hasAuthority("SCOPE_message.read")
            .and()
            .oauth2ResourceServer()
            .jwt();

            return http.build();
    }

    private Mono<ServerWebExchangeMatcher.MatchResult> securityMatcher(ServerWebExchange exchange) {
        return exchange.getPrincipal()
            .filter(p -> p instanceof OAuth2AccessToken)
            .hasElement()
            .flatMap(b -> b ? match() : notMatch());
    }
    // @formatter:on

    @Bean
    public HttpExchangeRepository tracesRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}
