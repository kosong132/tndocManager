package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.model.Token;
import com.example.model.User;
import com.example.repository.LoginRepository;
import com.example.repository.TokenRepository;

@Service
public class LoginService {

    private final LoginRepository loginRepository;
    private final TokenRepository tokenRepository; // Repository for managing tokens
    private final JavaMailSender emailSender;

    @Autowired
    public LoginService(LoginRepository loginRepository, TokenRepository tokenRepository, JavaMailSender emailSender) {
        this.loginRepository = loginRepository;
        this.tokenRepository = tokenRepository;
        this.emailSender = emailSender;
    }

    // Validate login credentials
    public boolean validateLogin(String username, String password) {
        Optional<User> user = loginRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    // Send password reset link
    public boolean sendPasswordResetLink(String email) {
        // Check if the user exists
        Optional<User> user = loginRepository.findByEmail(email);
        if (user.isEmpty()) {
            System.out.println("Email not found: " + email);
            return false;
        }
        

        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Save the token to the database with an expiration time
        Token resetToken = new Token();
        resetToken.setToken(token);
        resetToken.setAssociatedEmail(email);
        resetToken.setExpirationTime(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        tokenRepository.save(resetToken);

        // Construct reset link
        String resetLink = "http://localhost:4200/auth/reset-password/" + token;

        // Send the reset link to the user's email
        return sendResetEmail(email, resetLink);
    }

    // Send email with reset link
    private boolean sendResetEmail(String email, String resetLink) {
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, click the link below:\n" + resetLink);
            emailSender.send(message);
            return true; // Email sent successfully
        } catch (MailException e) {
            return false; // Email sending failed
        }
    }

    // Validate reset token
    public boolean validateResetToken(String token) {
        Optional<Token> resetToken = tokenRepository.findByToken(token);
        if (resetToken.isEmpty()) {
            System.out.println("Token not found: " + token);
            return false;
        }
        if (resetToken.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            System.out.println("Token expired: " + token);
            return false;
        }
        
        // Check if the token exists and is not expired
        return resetToken.isPresent() && resetToken.get().getExpirationTime().isAfter(LocalDateTime.now());
    }

    // Reset the user's password
    public boolean resetPassword(String token, String newPassword) {
        Optional<Token> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isEmpty() || resetToken.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            return false; // Invalid or expired token
        }

        // Find the user associated with the token
        String email = resetToken.get().getAssociatedEmail();
        Optional<User> user = loginRepository.findByEmail(email);

        if (user.isPresent()) {
            // Update the user's password
            User existingUser = user.get();
            existingUser.setPassword(newPassword); // Hash the password before saving in a real app
            loginRepository.save(existingUser);

            // Delete the used token
            tokenRepository.delete(resetToken.get());
            return true;
        }

        return false; // User not found
    }
}
