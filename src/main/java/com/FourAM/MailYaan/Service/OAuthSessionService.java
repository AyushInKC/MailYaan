package com.FourAM.MailYaan.Service;

import com.FourAM.MailYaan.Model.OAuthModel;
import com.FourAM.MailYaan.Repository.OAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class OAuthSessionService {

    @Autowired
    private OAuthRepository repository;

    public void saveToken(String email, String accessToken) {
        OAuthModel session = new OAuthModel();
        session.setEmail(email);
        session.setAccessToken(accessToken);
        session.setTokenStoredAt(LocalDateTime.now());
        repository.save(session);
    }

    public boolean isAuthenticated(String email) {
        return repository.existsByEmail(email);
    }

    public String findToken(String email) {
        OAuthModel session = repository.findByEmail(email);
        return session != null ? session.getAccessToken() : null;
    }

    public void saveToken(String email, String token, Instant expiresAt) {
        OAuthModel session = new OAuthModel();
        session.setEmail(email);
        session.setAccessToken(token);
        session.setExpiresAt(expiresAt);
        session.setTokenStoredAt(LocalDateTime.now());

        repository.save(session);
    }

    public OAuthModel getTokenByEmail(String email) {
        return repository.findByEmail(email);
    }

    public boolean isTokenExpired(OAuthModel model) {
        return model.getExpiresAt().isBefore(Instant.now());
    }
}
