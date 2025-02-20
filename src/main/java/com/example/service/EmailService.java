package com.example.service;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public String sendEmailWithAttachment(String recipientEmail, MultipartFile zipFile) {
        try {
            LOGGER.info("Checking if email exists in database: {}", recipientEmail);
            Optional<com.example.model.User> user = userRepository.findByEmail(recipientEmail);

            if (user.isEmpty()) {
                LOGGER.warn("❌ ERROR: Recipient email {} does not exist in the database.", recipientEmail);
                return "Recipient email does not exist in the database.";
            }

            LOGGER.info("✅ Email {} found, proceeding with sending email...", recipientEmail);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("EDI Files Attachment");
            helper.setText("Please find the attached EDI ZIP file.");

            // Check if file is empty
            if (zipFile.isEmpty()) {
                LOGGER.warn("❌ ERROR: Received an empty ZIP file.");
                return "Error: ZIP file is empty.";
            }

            LOGGER.info("✅ Attaching ZIP file: {}", zipFile.getOriginalFilename());
            helper.addAttachment(zipFile.getOriginalFilename(), new ByteArrayResource(zipFile.getBytes()));

            LOGGER.info("📩 Sending email...");
            mailSender.send(message);
            LOGGER.info("✅ Email sent successfully to {}!", recipientEmail);
            return "Email sent successfully.";
        } catch (MessagingException | IOException | MailException e) {
            LOGGER.error("❌ ERROR: Exception occurred while sending email: {}", e.getMessage(), e);
            return "Error sending email: " + e.getMessage();
        }
    }
}
