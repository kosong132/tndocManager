package com.example.controller;

import com.example.model.EdiData;
import com.example.service.EdiGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/edi-generator")
public class EdiGeneratorController {

    @Autowired
    private EdiGeneratorService ediGeneratorService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            List<EdiData> ediDataList = ediGeneratorService.processExcelFile(file);
            return ResponseEntity.ok(ediDataList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }
}
