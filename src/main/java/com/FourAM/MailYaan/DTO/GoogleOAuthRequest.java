package com.FourAM.MailYaan.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter

public class GoogleOAuthRequest {
    private String email;
    private String accessToken;
    private Instant expiresAt;
}
