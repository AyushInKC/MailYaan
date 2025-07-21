package com.FourAM.MailYaan.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
@Getter
@Setter
@Document(collection = "oauth_sessions")

public class OAuthModel {
    @Id
    private String id;
    private String email;
    private String accessToken;
    private LocalDateTime tokenStoredAt;
    private Instant expiresAt;

}
