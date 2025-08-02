package com.FourAM.MailYaan.Security;

import com.FourAM.MailYaan.Model.OAuthModel;
import com.FourAM.MailYaan.Repository.OAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OAuthRepository oAuthRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId(); // should be "google"
        String principalName = oauthToken.getName();

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                registrationId, principalName);

        if (client != null) {
            String accessToken = client.getAccessToken().getTokenValue();
            Instant expiresAt = client.getAccessToken().getExpiresAt();
            LocalDateTime storedAt = LocalDateTime.now();

            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email");

            // Save or update token
            OAuthModel model = oAuthRepository.findByEmail(email);
            if (model == null) {
                model = new OAuthModel();
                model.setEmail(email);
            }
            model.setAccessToken(accessToken);
            model.setExpiresAt(expiresAt);
            model.setTokenStoredAt(storedAt);

            oAuthRepository.save(model);

            // Redirect to dashboard
            response.sendRedirect("/api/dashboard");
        } else {
            response.sendRedirect("/error");
        }
    }
}
