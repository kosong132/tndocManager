package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.service.EmailService;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:4200")
public class EmailController {

    @Autowired
    private EmailService emailService;  // ✅ Ensure this is injected properly

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @RequestParam("email") String recipientEmail,
            @RequestParam("zipFile") MultipartFile zipFile) {
        try {
            String result = emailService.sendEmailWithAttachment(recipientEmail, zipFile);
            return result.contains("successfully") ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
