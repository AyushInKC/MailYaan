package com.FourAM.MailYaan.Controller;

import com.FourAM.MailYaan.DTO.GoogleOAuthRequest;
import com.FourAM.MailYaan.Service.OAuthSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class GoogleOAuthController {


    private OAuthSessionService sessionService;
    GoogleOAuthController(OAuthSessionService sessionService){
        this.sessionService=sessionService;
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "You are now logged in. MailYaan dashboard goes here.";
    }

//    @PostMapping("/google")
//    public ResponseEntity<String> receiveGoogleToken(@RequestBody GoogleOAuthRequest request) {
//        String email = request.getEmail();
//        String token = request.getAccessToken();
//
//        System.out.println("Received Google Email: " + email);
//        System.out.println("Received Access Token: " + token);
//
//        sessionService.saveToken(email, token);
//        return ResponseEntity.ok("Google OAuth2 token received and stored successfully");}

    @GetMapping("/user")
    public String getCurrentUser(@AuthenticationPrincipal OAuth2User user) {
        String email = user.getAttribute("email");
        return "Logged in as: " + email;
    }

    @GetMapping("/test-save")
    public String testSave() {
        sessionService.saveToken("ayush@example.com", "dummy-token", Instant.now().plusSeconds(3600));
        return "Saved dummy token";
    }

}
