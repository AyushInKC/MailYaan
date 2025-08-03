package com.FourAM.MailYaan.Controller;

import com.FourAM.MailYaan.Model.EmailHistory;
import com.FourAM.MailYaan.Model.EmailModel;
import com.FourAM.MailYaan.Repository.EmailHistoryRepository;
import com.FourAM.MailYaan.Service.GmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    private final GmailService gmailService;
    private final EmailHistoryRepository historyRepository;

    MailController(GmailService gmailService, EmailHistoryRepository historyRepository) {
        this.gmailService = gmailService;
        this.historyRepository = historyRepository;
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                       @RequestBody EmailModel emailModel) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // remove "Bearer "
                logger.info("Using provided Bearer token");
                gmailService.sendEmailWithToken(token, emailModel);
            } else {
                logger.warn("Missing Bearer token in request");
                return ResponseEntity.status(401).body("Missing Bearer token");
            }

            // Save history
            EmailHistory history = new EmailHistory();
            history.setSendersEmail(emailModel.getSendersEmail());
            history.setReceiversEmail(emailModel.getReceiversEmail());
            history.setSubject(emailModel.getSubject());
            history.setDescription(emailModel.getDescription());
            history.setSentAt(Instant.now());
            historyRepository.save(history);

            return ResponseEntity.ok("Email sent and logged successfully");
        } catch (Exception e) {
            logger.error("Failed to send email", e);
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/email-history")
    public ResponseEntity<List<EmailHistory>> emailHistory(@RequestHeader("Authorization") String authHeader) {
        // You can decode the token to extract the email if needed
        return ResponseEntity.ok(historyRepository.findAll());
    }
}
