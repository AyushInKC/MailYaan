package com.FourAM.MailYaan.Controller;

import com.FourAM.MailYaan.DTO.ScheduleRequest;
import com.FourAM.MailYaan.DTO.SendNowRequest;
import com.FourAM.MailYaan.Service.GmailService;
import com.FourAM.MailYaan.Service.GmailService;
import com.FourAM.MailYaan.Service.OAuthSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/email")
public class MailController {


    private GmailService gmailService;
    private OAuthSessionService sessionService;

    MailController(GmailService gmailService,OAuthSessionService oAuthSessionService){
        this.sessionService=oAuthSessionService;
        this.gmailService=gmailService;
    }

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendMail(@RequestParam String userEmail,
                                           @RequestParam String to,
                                           @RequestParam String subject,
                                           @RequestParam String message) {
        try {
            return gmailService.sendMailAsUser(userEmail, to, subject, message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


//    @PostMapping("/schedule")
//    public String scheduleEmail(@RequestHeader("X-User-Email") String userEmail,
//                                @RequestBody ScheduleRequest request) {
//
//        if (!sessionService.isAuthenticated(userEmail)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OAuth2 session not found");
//        }
//
//        request.setSendersEmail(userEmail);
//
//        return mailService.scheduleEmail(request);
//    }


}
