package com.FourAM.MailYaan.Configuration;

import com.FourAM.MailYaan.Resolver.CustomAuthorizationRequestResolver;
import com.FourAM.MailYaan.Security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class OAuthSecurityConfiguration {

    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthorizationRequestResolver customResolver =
                new CustomAuthorizationRequestResolver(clientRegistrationRepository);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/error", "/webjars/**", "/oauth2/**", "/api/debug/**").permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authEndpoint -> authEndpoint.authorizationRequestResolver(customResolver))
                        .successHandler(oAuth2LoginSuccessHandler)
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .opaqueToken(token -> token
                                .introspector(googleOpaqueTokenIntrospector())
                        )
                );

        return http.build();
    }
    @Bean
    public OpaqueTokenIntrospector googleOpaqueTokenIntrospector() {
        return token -> {
            // Call Google tokeninfo endpoint to verify token
            var url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=" + token;
            try (var in = new java.net.URL(url).openStream()) {
                var json = new String(in.readAllBytes());
                if (json.contains("error")) {
                    throw new RuntimeException("Invalid Google token: " + json);
                }
                return new org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal(
                        java.util.Map.of("active", true, "sub", token), java.util.List.of()
                );
            } catch (Exception e) {
                throw new RuntimeException("Token validation failed", e);
            }
        };
    }
}
