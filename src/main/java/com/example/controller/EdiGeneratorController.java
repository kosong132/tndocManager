package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.EdiData;
import com.example.model.User;
import com.example.service.EdiGeneratorService;
import com.example.service.EmailService;
import com.example.service.UserService;

@RestController
@CrossOrigin(origins = "*") // Enable CORS for Angular app
@RequestMapping("/api/edi-generator")
public class EdiGeneratorController {

    @Autowired
    private EdiGeneratorService ediGeneratorService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            List<EdiData> ediDataList = ediGeneratorService.processExcelFile(file);
            ediDataList.forEach(data -> System.out.println(data.toString()));
            return ResponseEntity.ok().body(ediDataList);
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @RequestParam("email") String recipientEmail,
            @RequestParam("zipFile") MultipartFile zipFile) {
        try {
            Optional<User> user = userService.findByEmail(recipientEmail);
    
            if (user.isEmpty()) {
                System.out.println("❌ Email not found: " + recipientEmail);
                return ResponseEntity.badRequest().body("Recipient email does not exist.");
            }
    
            System.out.println("✅ Email exists: " + recipientEmail + " | Username: " + user.get().getUsername());
            String result = emailService.sendEmailWithAttachment(recipientEmail, zipFile);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
    

    @GetMapping("/validate-email")
    public ResponseEntity<?> validateEmail(@RequestParam("email") String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            System.out.println("✅ Email exists: " + email + " | Username: " + user.get().getUsername());
            return ResponseEntity.ok().body("{\"valid\": true, \"username\": \"" + user.get().getUsername() + "\"}");
        } else {
            System.out.println("❌ Email not found: " + email);
            return ResponseEntity.ok().body("{\"valid\": false}");
        }
    }
}
