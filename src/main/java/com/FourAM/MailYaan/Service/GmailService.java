package com.FourAM.MailYaan.Service;
import com.google.api.services.gmail.model.Message;
import com.FourAM.MailYaan.Model.EmailModel;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class GmailService {
    private static final String APPLICATION_NAME = "MailYaan";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final OAuth2AuthorizedClientService authorizedClientService;

    public GmailService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    public void sendEmail(EmailModel emailModel) throws Exception {

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient("google", emailModel.getSendersEmail());
        if (client == null) {
            throw new IllegalStateException("User not logged in or authorized");
        }

        String accessToken = client.getAccessToken().getTokenValue();

        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Gmail gmail = new Gmail.Builder(httpTransport, JSON_FACTORY,
                request -> request.getHeaders().setAuthorization("Bearer " + accessToken))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String rawEmail = buildRawEmail(emailModel);

        Message message = new Message();
        message.setRaw(Base64.getUrlEncoder().encodeToString(rawEmail.getBytes(StandardCharsets.UTF_8)));

        gmail.users().messages().send("me", message).execute();
    }

    private String buildRawEmail(EmailModel emailModel) {
        String toHeader = String.join(", ", emailModel.getReceiversEmail());
        String bodyText = String.join("\n", emailModel.getDescription());

        StringBuilder sb = new StringBuilder();
        sb.append("From: ").append(emailModel.getSendersEmail()).append("\r\n");
        sb.append("To: ").append(toHeader).append("\r\n");
        sb.append("Subject: ").append(emailModel.getSubject()).append("\r\n");
        sb.append("Content-Type: text/plain; charset=\"UTF-8\"\r\n");
        sb.append("\r\n");
        sb.append(bodyText).append("\r\n");

        return sb.toString();
    }
}
