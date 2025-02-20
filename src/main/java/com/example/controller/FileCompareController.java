package com.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.TagData;
import com.example.service.FileParserService;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*") // Update with the Angular frontend URL
public class FileCompareController {

    private final FileParserService parserService;

    @Autowired
    public FileCompareController(FileParserService parserService) {
        this.parserService = parserService;
    }

    @PostMapping("/preview")
    @ResponseBody
    public List<TagData> previewFilesJson(@RequestParam("cusdecFile") MultipartFile cusdecFile,
                                          @RequestParam("cusresFile") MultipartFile cusresFile) {
        try {
            // Read content from both files
            String cusdecContent = readFileContent(cusdecFile);
            String cusresContent = readFileContent(cusresFile);
    
            // Parse the content of both files
            List<TagData> cusdecData = parserService.parseCusdecContent(cusdecContent);
            List<TagData> cusresData = parserService.parseCusresContent(cusresContent);
            
            // Merge data
            return mergeDataWithDuplicates(cusdecData, cusresData);
        } catch (Exception e) {
            throw new RuntimeException("Error processing files: " + e.getMessage());
        }
    }
    

  private List<TagData> mergeDataWithDuplicates(List<TagData> cusdecData, List<TagData> cusresData) {
        List<TagData> mergedData = new ArrayList<>();

        // Create a map to track tags from CUSDEC and CUSRES
        Map<String, List<String>> cusdecMap = new HashMap<>();
        Map<String, List<String>> cusresMap = new HashMap<>();

        // Populate the CUSDEC map
        for (TagData data : cusdecData) {
            cusdecMap.computeIfAbsent(data.getTag(), k -> new ArrayList<>()).add(data.getCusdecData());
        }

        // Populate the CUSRES map
        for (TagData data : cusresData) {
            cusresMap.computeIfAbsent(data.getTag(), k -> new ArrayList<>()).add(data.getCusresData());
        }

        // Merge the two maps, preserving duplicates
        Set<String> allTags = new LinkedHashSet<>();
        allTags.addAll(cusdecMap.keySet());
        allTags.addAll(cusresMap.keySet());

        for (String tag : allTags) {
            List<String> decDataList = cusdecMap.getOrDefault(tag, Collections.emptyList());
            List<String> resDataList = cusresMap.getOrDefault(tag, Collections.emptyList());

            int maxSize = Math.max(decDataList.size(), resDataList.size());
            for (int i = 0; i < maxSize; i++) {
                String cusdecDataEntry = i < decDataList.size() ? decDataList.get(i) : "N/A";
                String cusresDataEntry = i < resDataList.size() ? resDataList.get(i) : "N/A";
                mergedData.add(new TagData(tag, cusdecDataEntry, cusresDataEntry));
            }
        }

        return mergedData;
    }

    private String readFileContent(MultipartFile file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}


    // @GetMapping("/export/excel")
    // public void exportToExcel(HttpServletResponse response) {
    //     try {
    //         if (combinedTagData == null || combinedTagData.isEmpty()) {
    //             throw new IllegalStateException("No data available for export.");
    //         }
    //         response.setContentType("application/vnd.ms-excel");
    //         response.setHeader("Content-Disposition", "attachment; filename=TagData.xlsx");
    //         excelExportService.exportToExcel(combinedTagData, response.getOutputStream());
    //     } catch (Exception e) {
    //         throw new RuntimeException("Failed to export data to Excel: " + e.getMessage());
    //     }
    // }
