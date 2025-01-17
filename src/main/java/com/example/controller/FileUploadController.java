// package com.example.controller;


// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;
// import org.w3c.dom.Document;
// import org.w3c.dom.NodeList;

// import javax.xml.parsers.DocumentBuilder;
// import javax.xml.parsers.DocumentBuilderFactory;
// import java.io.InputStream;
// import java.util.HashMap;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/files")
// @CrossOrigin(origins = "http://localhost:4200") // Adjust based on Angular's running port
// public class FileUploadController {

//     @PostMapping("/upload")
//     public ResponseEntity<Map<String, String>> uploadFile(@RequestParam MultipartFile file) {
//         Map<String, String> fieldMappings = new HashMap<>();

//         if (file.isEmpty()) {
//             return ResponseEntity.badRequest().body(null);
//         }

//         try (InputStream inputStream = file.getInputStream()) {
//             // Parse the XML file
//             DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//             DocumentBuilder builder = factory.newDocumentBuilder();
//             Document document = builder.parse(inputStream);

//             // Extract fields and values (customize this based on your XML structure)
//             NodeList fields = document.getElementsByTagName("field");
//             NodeList values = document.getElementsByTagName("value");

//             for (int i = 0; i < fields.getLength(); i++) {
//                 fieldMappings.put(fields.item(i).getTextContent(), values.item(i).getTextContent());
//             }

//             return ResponseEntity.ok(fieldMappings);
//         } catch (Exception e) {
//             return ResponseEntity.internalServerError().body(null);
//         }
//     }
// }
