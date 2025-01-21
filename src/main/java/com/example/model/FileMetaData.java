// package com.example.model;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "file_metadata")
// public class FileMetaData {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(name = "file_name", nullable = false)
//     private String fileName;

//     @Column(name = "file_path", nullable = false)
//     private String filePath;

//     @Column(name = "file_size", nullable = false)
//     private long fileSize;

//     public FileMetaData() {}

//     public FileMetaData(String fileName, String filePath, long fileSize) {
//         this.fileName = fileName;
//         this.filePath = filePath;
//         this.fileSize = fileSize;
//     }

//     public Long getId() {
//         return id;
//     }

//     public String getFileName() {
//         return fileName;
//     }

//     public String getFilePath() {
//         return filePath;
//     }

//     public long getFileSize() {
//         return fileSize;
//     }
// }
