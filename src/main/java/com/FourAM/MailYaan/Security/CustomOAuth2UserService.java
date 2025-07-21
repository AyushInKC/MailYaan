package com.FourAM.MailYaan.Security;

import com.FourAM.MailYaan.Service.OAuthSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    @Autowired
    private OAuthSessionService oauthSessionService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        logger.info("🧠 Attributes received from OAuth2User: {}", oAuth2User.getAttributes());

        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            logger.warn("❌ Email attribute is null!");
            email = "unknown@example.com";
        }

        OAuth2AccessToken accessToken = userRequest.getAccessToken();

        logger.info("📧 Email: {}", email);
        logger.info("🔐 Access Token: {}", accessToken.getTokenValue());
        logger.info("⏳ Expires At: {}", accessToken.getExpiresAt());

        oauthSessionService.saveToken(email, accessToken.getTokenValue(), accessToken.getExpiresAt());

        logger.info("✅ Token saved successfully for {}", email);

        return oAuth2User;

    }
}
