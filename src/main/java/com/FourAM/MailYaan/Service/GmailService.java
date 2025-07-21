package com.FourAM.MailYaan.Service;

import com.FourAM.MailYaan.Model.OAuthModel;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;

@Service
public class GmailService {

    @Autowired
    private OAuthSessionService oauthService;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> sendMailAsUser(String userEmail, String to, String subject, String bodyText) throws Exception {
        OAuthModel tokenData = oauthService.getTokenByEmail(userEmail);

        if (tokenData == null || oauthService.isTokenExpired(tokenData)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token missing or expired");
        }

        String accessToken = tokenData.getAccessToken();
        String rawEmail = createRawEmail(to, subject, bodyText);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = "{\"raw\": \"" + rawEmail + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        return restTemplate.postForEntity(
                "https://gmail.googleapis.com/gmail/v1/users/me/messages/send",
                entity,
                String.class
        );
    }

    private String createRawEmail(String to, String subject, String bodyText) throws Exception {
        Properties props = new Properties();
        Session session = Session.getInstance(props, null);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("me@gmail.com")); // 'me' works for Gmail API
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(bodyText);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        message.writeTo(buffer);

        return Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
    }
}
