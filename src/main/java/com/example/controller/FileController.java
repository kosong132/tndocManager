// package com.example.controller;

// import com.example.model.TagData;
// import com.example.service.FileParserService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.util.List;


// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.nio.charset.StandardCharsets;
// import java.util.ArrayList;

// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/files")
// @CrossOrigin(origins = "http://localhost:4200") // Update with the Angular frontend URL
// public class FileController {

//     private final FileParserService parserService;

//     @Autowired
//     public FileController(FileParserService parserService) {
//         this.parserService = parserService;
//     }

//     @PostMapping("/preview")
//     @ResponseBody
//     public List<TagData> previewFilesJson(@RequestParam("cusdecFile") MultipartFile cusdecFile,
//                                           @RequestParam("cusresFile") MultipartFile cusresFile) {
//         try {
//             // Read content from both files
//             String cusdecContent = readFileContent(cusdecFile);
//             String cusresContent = readFileContent(cusresFile);
    
//             // Parse the content of both files
//             List<TagData> cusdecData = parserService.parseCusdecContent(cusdecContent);
//             List<TagData> cusresData = parserService.parseCusresContent(cusresContent);
    
//             // Merge data
//             return mergeData(cusdecData, cusresData);
//         } catch (Exception e) {
//             throw new RuntimeException("Error processing files: " + e.getMessage());
//         }
//     }
    

//     private List<TagData> mergeData(List<TagData> cusdecData, List<TagData> cusresData) {
//         List<TagData> mergedData = new ArrayList<>();
//         for (TagData tag : cusdecData) {
//             mergedData.add(tag);
//         }
//         mergedData.addAll(cusresData);
//         return mergedData;
//     }

//     private String readFileContent(MultipartFile file) throws Exception {
//         try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
//             return reader.lines().collect(Collectors.joining("\n"));
//         }
//     }
// }


//     // @GetMapping("/export/excel")
//     // public void exportToExcel(HttpServletResponse response) {
//     //     try {
//     //         if (combinedTagData == null || combinedTagData.isEmpty()) {
//     //             throw new IllegalStateException("No data available for export.");
//     //         }
//     //         response.setContentType("application/vnd.ms-excel");
//     //         response.setHeader("Content-Disposition", "attachment; filename=TagData.xlsx");
//     //         excelExportService.exportToExcel(combinedTagData, response.getOutputStream());
//     //     } catch (Exception e) {
//     //         throw new RuntimeException("Failed to export data to Excel: " + e.getMessage());
//     //     }
//     // }
