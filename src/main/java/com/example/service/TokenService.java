package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Token;
import com.example.repository.TokenRepository;

import java.time.LocalDateTime;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public boolean validateToken(String token) {
        // Check if the token exists
        return tokenRepository.findByToken(token)
                .filter(this::isTokenValid)
                .isPresent();
    }

    private boolean isTokenValid(Token tokenEntity) {
        // Check if the token has expired
        return tokenEntity.getExpirationTime().isAfter(LocalDateTime.now());
    }

    // Optional: Method to create a new token
    public Token createToken(String email) {
        Token token = new Token();
        token.setToken(generateRandomToken()); // Use your method to generate a token
        token.setAssociatedEmail(email);
        token.setExpirationTime(LocalDateTime.now().plusHours(1)); // Example: 1-hour validity
        return tokenRepository.save(token);
    }

    // Helper method to generate a random token (e.g., UUID)
    private String generateRandomToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
