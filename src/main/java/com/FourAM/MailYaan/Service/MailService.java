//// --- GmailService.java ---
//package com.FourAM.MailYaan.Service;
//
//import com.FourAM.MailYaan.DTO.SendNowRequest;
//import com.FourAM.MailYaan.Model.EmailModel;
//import com.google.api.services.gmail.Gmail;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Service
//public class MailService {
//
//    @Autowired
//    private OAuthSessionService sessionService;
//
//    @Autowired
//    private GmailService gmailService;
//
//    private static final Logger logger = Logger.getLogger(MailService.class.getName());
//
//    public String sendNow(SendNowRequest request) {
//        if (request.getReceiversEmail().size() != request.getDescription().size()) {
//            return "Error: Number of recipients and descriptions must match!";
//        }
//
//        String sender = request.getSendersEmail();
//        String token = sessionService.findToken(sender);
//        if (token == null) {
//            return "Error: Sender not authenticated via OAuth2.";
//        }
//
//        try {
//            Gmail gmail = gmailService.getGmailService(token);
//            for (int i = 0; i < request.getReceiversEmail().size(); i++) {
//                MimeMessage email = gmailService.createEmail(
//                        request.getReceiversEmail().get(i),
//                        sender,
//                        request.getSubject(),
//                        request.getDescription().get(i)
//                );
//                gmailService.sendMessage(gmail, "me", email);
//            }
//            return "Email(s) sent via Gmail API.";
//        } catch (Exception e) {
//            logger.log(Level.SEVERE, "Failed to send email via Gmail API for sender: " + sender, e);
//            return "Error sending email: " + e.getMessage();
//        }
//    }
//}