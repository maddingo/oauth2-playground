package no.lyse.plattform.oauthplayground.client.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {
    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

//    @Bean
//    WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/webjars/**");
//    }

    // @formatter:off
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/error/**", "/webjars/**", "").permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(withDefaults())
//            .oauth2Login(oauth2Login ->
//                oauth2Login.loginPage("/oauth2/authorization/messaging-client-oidc")
//            )
            .oauth2Client(withDefaults())
            .logout(logout ->
                logout.logoutSuccessHandler(oidcLogoutSuccessHandler())
            )
            .csrf(csrf -> csrf.disable())
            .build();
    }
    // @formatter:on

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
            new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        // Set the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/index");

        return oidcLogoutSuccessHandler;
    }
}
