// package com.example.service;

// import com.example.model.TagData;
// import org.apache.poi.ss.usermodel.*;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.springframework.stereotype.Service;


// import java.io.OutputStream;
// import java.util.List;


// @Service
// public class ExcelExportService {


//     public void exportToExcel(List<TagData> tagDataList, OutputStream outputStream) {
//         try (Workbook workbook = new XSSFWorkbook()) {
//             Sheet sheet = workbook.createSheet("Tag Data");


//             // Create Header Row
//             Row headerRow = sheet.createRow(0);
//             CellStyle headerStyle = workbook.createCellStyle();
//             Font headerFont = workbook.createFont();
//             headerFont.setBold(true);
//             headerStyle.setFont(headerFont);


//             String[] headers = {"Tag", "CUSDEC Data", "CUSRES Data"};
//             for (int i = 0; i < headers.length; i++) {
//                 Cell cell = headerRow.createCell(i);
//                 cell.setCellValue(headers[i]);
//                 cell.setCellStyle(headerStyle);
//             }


//             // Populate Data Rows
//             for (int i = 0; i < tagDataList.size(); i++) {
//                 TagData tagData = tagDataList.get(i);
//                 Row dataRow = sheet.createRow(i + 1);


//                 dataRow.createCell(0).setCellValue(tagData.getTag());
//                 dataRow.createCell(1).setCellValue(tagData.getCusdecData());
//                 dataRow.createCell(2).setCellValue(tagData.getCusresData());
//             }


//             // Auto-size columns
//             for (int i = 0; i < headers.length; i++) {
//                 sheet.autoSizeColumn(i);
//             }


//             // Write to output stream
//             workbook.write(outputStream);
//         } catch (Exception e) {
//             throw new RuntimeException("Failed to write data to Excel file: " + e.getMessage());
//         }
//     }
// }


