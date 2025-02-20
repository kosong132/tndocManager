package com.example.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.XMLField;
import com.example.service.XmlService;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/xml")
public class XMLController {

    @Autowired
    private XmlService xmlService;

    private String uploadedFilePath;

    @PostMapping("/upload")
    public ResponseEntity<List<XMLField>> uploadAndParseFile(@RequestParam("file") MultipartFile file) {
        try {
            // Save file to local directory
            String uploadDirPath = "C:/uploads";
            File uploadsDir = new File(uploadDirPath);

            if (!uploadsDir.exists() && !uploadsDir.mkdirs()) {
                throw new IOException("Failed to create upload directory.");
            }

            File savedFile = new File(uploadsDir, file.getOriginalFilename());
            file.transferTo(savedFile);

            uploadedFilePath = savedFile.getAbsolutePath();

            // Parse the file and return results
            List<XMLField> xmlFields = xmlService.parseXmlFile(uploadedFilePath);
            return ResponseEntity.ok(xmlFields);

        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) {
        try {
            if (uploadedFilePath != null) {
                List<XMLField> xmlFields = xmlService.parseXmlFile(uploadedFilePath);
                xmlService.exportToExcel(response, xmlFields, "ExportedXMLData");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded for export.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
