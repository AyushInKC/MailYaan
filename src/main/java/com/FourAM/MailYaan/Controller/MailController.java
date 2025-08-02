package com.FourAM.MailYaan.Controller;

import com.FourAM.MailYaan.Model.EmailHistory;
import com.FourAM.MailYaan.Model.EmailModel;
import com.FourAM.MailYaan.Repository.EmailHistoryRepository;
import com.FourAM.MailYaan.Service.GmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MailController {


    private GmailService gmailService;
//    private OAuthSessionService sessionService;
    private EmailHistoryRepository historyRepository;

    MailController(GmailService gmailService,EmailHistoryRepository historyRepository){
//        this.sessionService=oAuthSessionService;
        this.gmailService=gmailService;
        this.historyRepository=historyRepository;
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@AuthenticationPrincipal OAuth2User principal,
                                       @RequestBody EmailModel emailModel) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String userEmail = principal.getAttribute("email");
        if (!userEmail.equalsIgnoreCase(emailModel.getSendersEmail())) {
            return ResponseEntity.status(403).body("Sender email must match logged-in user");
        }

        try {
            gmailService.sendEmail(emailModel);
            EmailHistory history = new EmailHistory();
            history.setSendersEmail(emailModel.getSendersEmail());
            history.setReceiversEmail(emailModel.getReceiversEmail());
            history.setSubject(emailModel.getSubject());
            history.setDescription(emailModel.getDescription());
            history.setSentAt(Instant.now());

            historyRepository.save(history);

            return ResponseEntity.ok("Email sent and logged successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/email-history")
    public ResponseEntity<List<EmailHistory>> emailHistory(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String userEmail = principal.getAttribute("email");

        List<EmailHistory> history = historyRepository.findBySendersEmailOrderBySentAtDesc(userEmail);
        return ResponseEntity.ok(history);
    }
}

