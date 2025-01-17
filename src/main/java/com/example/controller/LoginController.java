package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Login;
import com.example.model.LoginResponse;
import com.example.service.LoginService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Allow requests from Angular frontend 
public class LoginController {

    private LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Login login) {
        boolean isValid = loginService.validateLogin(login.getUsername(), login.getPassword());

        if (isValid) {
            // Replace this with actual token generation logic (e.g., JWT token)
            String token = "your-jwt-token-here";  // This should be generated dynamically

            // Return a structured JSON response
            LoginResponse response = new LoginResponse("Login successful!", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(new LoginResponse("Invalid username or password.", null));
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
    
        // Log the incoming request
        System.out.println("Received forgot-password request for email: " + email);
    
        if (email == null || email.isEmpty()) {
            System.out.println("Email is null or empty.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email is required.");
        }
    
        boolean resetSuccess = loginService.sendPasswordResetLink(email);
        if (resetSuccess) {
            System.out.println("Password reset link sent successfully to: " + email);
            return ResponseEntity.ok("Password reset link sent to email.");
        } else {
            System.out.println("Failed to send password reset link for email: " + email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to send password reset link. Please check the email address.");
        }
    }
    

@PostMapping("/reset-password/{token}")
public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody Map<String, String> requestBody) {
    String newPassword = requestBody.get("newPassword");

    if (newPassword == null || newPassword.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("message", "New password is required."));
    }

    boolean resetSuccess = loginService.resetPassword(token, newPassword);

    if (resetSuccess) {
        return ResponseEntity.ok(Map.of("message", "Password has been reset successfully!"));
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Invalid or expired token."));
    }
}


}
