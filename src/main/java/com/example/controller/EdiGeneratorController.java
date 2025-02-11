package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.EdiData;
import com.example.service.EdiGeneratorService;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Enable CORS for Angular app
@RequestMapping("/api/edi-generator")
public class EdiGeneratorController {

    @Autowired
    private EdiGeneratorService ediGeneratorService;

@PostMapping("/upload")
public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
    try {
        List<EdiData> ediDataList = ediGeneratorService.processExcelFile(file);

        // Log processed data for debugging
        ediDataList.forEach(data -> System.out.println(data.toString()));

        return ResponseEntity.ok().body(ediDataList); // Return processed data
    } catch (Exception e) {
        System.err.println("Error processing file: " + e.getMessage());
        return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
    }
}

}
