// package com.example.service;

// import java.io.File;
// import java.io.FileInputStream;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.zip.ZipEntry;
// import java.util.zip.ZipOutputStream;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// // import com.example.model.FileMetaData;
// import com.example.repository.FileRepository;

// @Service
// public class FileSortService {

//     private static final String UPLOAD_DIR = "uploads/";

//     @Autowired
//     private FileRepository fileRepository;

//     public List<File> saveFiles(MultipartFile[] files) throws IOException {
//         List<File> savedFiles = new ArrayList<>();
//         Path uploadDir = Paths.get(UPLOAD_DIR);

//         if (!Files.exists(uploadDir)) {
//             Files.createDirectories(uploadDir);
//         }

//         for (MultipartFile file : files) {
//             Path filePath = uploadDir.resolve(file.getOriginalFilename());
//             Files.write(filePath, file.getBytes());
//             savedFiles.add(filePath.toFile());

//             // Save metadata to the database
//             FileMetaData metaData = new FileMetaData(file.getOriginalFilename(), filePath.toString(), file.getSize());
//             fileRepository.save(metaData);
//         }
//         return savedFiles;
//     }

//     public Map<String, List<File>> sortFilesByName(List<File> files) {
//         Map<String, List<File>> sortedFiles = new HashMap<>();
//         for (File file : files) {
//             String baseName = file.getName().substring(0, Math.min(9, file.getName().length()));
//             sortedFiles.computeIfAbsent(baseName, k -> new ArrayList<>()).add(file);
//         }
//         return sortedFiles;
//     }

//     public List<File> zipFiles(Map<String, List<File>> sortedFiles) throws IOException {
//         List<File> zippedFiles = new ArrayList<>();

//         for (Map.Entry<String, List<File>> entry : sortedFiles.entrySet()) {
//             String prefix = entry.getKey();
//             List<File> files = entry.getValue();

//             File zipFile = new File(UPLOAD_DIR + prefix + ".abv");
//             try (FileOutputStream fos = new FileOutputStream(zipFile);
//                  ZipOutputStream zos = new ZipOutputStream(fos)) {
//                 for (File file : files) {
//                     addToZipFile(file, zos);
//                 }
//             }
//             zippedFiles.add(zipFile);
//         }
//         return zippedFiles;
//     }

//     private void addToZipFile(File file, ZipOutputStream zos) throws IOException {
//         try (FileInputStream fis = new FileInputStream(file)) {
//             ZipEntry zipEntry = new ZipEntry(file.getName());
//             zos.putNextEntry(zipEntry);

//             byte[] bytes = new byte[1024];
//             int length;
//             while ((length = fis.read(bytes)) >= 0) {
//                 zos.write(bytes, 0, length);
//             }
//         }
//     }

//     public File createFinalZip(List<File> zippedFiles) throws IOException {
//         File finalZip = new File(UPLOAD_DIR + "all_abv_files.zip");
//         try (FileOutputStream fos = new FileOutputStream(finalZip);
//              ZipOutputStream zos = new ZipOutputStream(fos)) {
//             for (File zip : zippedFiles) {
//                 addToZipFile(zip, zos);
//             }
//         }
//         return finalZip;
//     }
// }
