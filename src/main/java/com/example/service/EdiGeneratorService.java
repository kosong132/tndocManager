package com.example.service;

import com.example.model.EdiData;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class EdiGeneratorService {

    public List<EdiData> processExcelFile(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            return readExcelSheet(sheet);
        }
    }

    private List<EdiData> readExcelSheet(Sheet sheet) {
        List<EdiData> ediDataList = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() == 0 || row.getRowNum() == 1) continue; // Skip header rows

            EdiData ediData = new EdiData();
            ediData.setSalesOrderNumber(getCellValue(row, 2));
            ediData.setShipmentDate(getCellValue(row, 3));
            ediData.setShipmentTime(getCellValue(row, 4));
            ediData.setShipmentNumber(getCellValue(row, 8));
            ediData.setShipmentStatusCode(getCellValue(row, 11));
            ediData.setShipmentAppointmentReasonCode(getCellValue(row, 12));
            ediData.setDONumber(getCellValue(row, 28));
            ediData.setDODate(getCellValue(row, 29));
            ediData.setConsigneeName(getCellValue(row, 41));
            ediData.setConsigneeAddress1(getCellValue(row, 42));
            ediData.setConsigneeAddress2(getCellValue(row, 43));
            ediData.setConsigneeCity(getCellValue(row, 44));
            ediData.setConsigneePostalCode(getCellValue(row, 46));
            ediData.setConsigneeStateCode(getCellValue(row, 45));
            ediData.setRoutingCode(getCellValue(row, 49));
            ediData.setFormattedShipmentDateYYMMDD(formatToYYMMDD(ediData.getShipmentDate()));
            ediData.setFormattedShipmentTimeHHMM(formatToHHMM(ediData.getShipmentTime()));

            ediDataList.add(ediData);
        }

        return ediDataList;
    }

    private String getCellValue(Row row, int cellIndex) {
        if (row.getCell(cellIndex) != null) {
            Cell cell = row.getCell(cellIndex);
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
                        return dateFormat.format(cell.getDateCellValue());
                    } else {
                        return String.valueOf((long) cell.getNumericCellValue());
                    }
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                default:
                    return "";
            }
        }
        return "";
    }

    private String formatToYYMMDD(String date) {
        if (date != null && date.length() == 8) {
            return date.substring(2); // Extract the last 6 characters for YYMMDD
        }
        return date;
    }

    private String formatToHHMM(String time) {
        if (time != null && time.length() == 6) {
            return time.substring(0, 4); // Extract the first 4 characters for HHMM
        }
        return time;
    }
}
