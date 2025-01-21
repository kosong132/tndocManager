package com.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:4200")
public class FileSortingController {
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/files")
    public ResponseEntity<ByteArrayResource> handleFileUpload(@RequestParam("files") MultipartFile[] files) {
        try {
            // Step 1: Save uploaded files
            List<File> savedFiles = saveUploadedFiles(files);

            // Step 2: Sort files by the first 9 characters of their name
            Map<String, List<File>> sortedFiles = sortFilesByName(savedFiles);

            // Step 3: Zip files into .abv archives
            List<File> zippedFiles = zipFiles(sortedFiles);

            // Step 4: Create a single zip containing all the .abv files for download
            File finalZipFile = createZipOfAbvFiles(zippedFiles);

            // Step 5: Return the final zip file containing all .abv files
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(finalZipFile.toPath()));

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"all_abv_files.zip\"")
                    .body(resource);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private List<File> saveUploadedFiles(MultipartFile[] files) throws IOException {
        List<File> savedFiles = new ArrayList<>();
        Path uploadDir = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        for (MultipartFile file : files) {
            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            savedFiles.add(filePath.toFile());
        }
        return savedFiles;
    }

    private Map<String, List<File>> sortFilesByName(List<File> files) {
        Map<String, List<File>> sortedFiles = new HashMap<>();
        for (File file : files) {
            String baseName = file.getName().substring(0, Math.min(9, file.getName().length()));
            sortedFiles.computeIfAbsent(baseName, k -> new ArrayList<>()).add(file);
        }
        return sortedFiles;
    }

    private List<File> zipFiles(Map<String, List<File>> sortedFiles) throws IOException {
        List<File> zippedFiles = new ArrayList<>();

        for (Map.Entry<String, List<File>> entry : sortedFiles.entrySet()) {
            String namePrefix = entry.getKey();
            List<File> filesToZip = entry.getValue();

            // Create a zip file for each group of files
            String zipFileName = namePrefix + ".abv";
            File zipFile = new File(UPLOAD_DIR + zipFileName);

            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                for (File file : filesToZip) {
                    addToZipFile(file, zos);
                }
            }

            zippedFiles.add(zipFile);
        }
        return zippedFiles;
    }

    private void addToZipFile(File file, ZipOutputStream zos) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
        }
    }

    private File createZipOfAbvFiles(List<File> abvFiles) throws IOException {
        String zipFileName = UPLOAD_DIR + "all_abv_files.zip";
        File finalZipFile = new File(zipFileName);

        try (FileOutputStream fos = new FileOutputStream(finalZipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (File abvFile : abvFiles) {
                addToZipFile(abvFile, zos);
            }
        }
        return finalZipFile;
    }
}