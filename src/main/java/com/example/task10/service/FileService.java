// package com.example.task10.service;


// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.BufferedReader;
// import java.io.InputStreamReader;

// @Service
// public class FileService {

//     public void processFile(MultipartFile file) throws Exception {
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 System.out.println("Processing line: " + line); // Simulate processing
//             }
//         }
//     }
// }