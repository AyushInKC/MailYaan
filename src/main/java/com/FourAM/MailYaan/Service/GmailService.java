package com.FourAM.MailYaan.Service;

import com.FourAM.MailYaan.Model.EmailModel;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class GmailService {

    private static final Logger logger = LoggerFactory.getLogger(GmailService.class);
    private static final String APPLICATION_NAME = "MailYaan";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    public void sendEmailWithToken(String accessToken, EmailModel emailModel) throws Exception {
        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Gmail gmail = new Gmail.Builder(httpTransport, JSON_FACTORY,
                request -> request.getHeaders().setAuthorization("Bearer " + accessToken))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String rawEmail = buildRawEmail(emailModel);
        Message message = new Message();
        message.setRaw(Base64.getUrlEncoder().encodeToString(rawEmail.getBytes(StandardCharsets.UTF_8)));

        var response = gmail.users().messages().send("me", message).execute();

        if (response.getId() == null) {
            throw new IllegalStateException("Gmail API returned unexpected response — possibly HTML login page");
        }

        logger.info("Email sent successfully: Message ID {}", response.getId());
    }

    private String buildRawEmail(EmailModel emailModel) {
        String toHeader = String.join(", ", emailModel.getReceiversEmail());
        String bodyText = String.join("\n", emailModel.getDescription());

        return "From: " + emailModel.getSendersEmail() + "\r\n" +
                "To: " + toHeader + "\r\n" +
                "Subject: " + emailModel.getSubject() + "\r\n" +
                "Content-Type: text/plain; charset=\"UTF-8\"\r\n" +
                "\r\n" +
                bodyText + "\r\n";
    }
}
