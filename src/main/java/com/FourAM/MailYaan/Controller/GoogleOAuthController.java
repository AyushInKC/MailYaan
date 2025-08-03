package com.FourAM.MailYaan.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GoogleOAuthController {

    private final OAuth2AuthorizedClientService clientService;

    public GoogleOAuthController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/debug/token")
    public ResponseEntity<?> debugToken(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }

        String userEmail = principal.getAttribute("email");

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient("google", userEmail);
        if (client == null) {
            return ResponseEntity.status(404).body("Token not found for " + userEmail);
        }

        return ResponseEntity.ok(client.getAccessToken().getTokenValue());
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "Welcome to Mailyaan!!!";
    }
}
