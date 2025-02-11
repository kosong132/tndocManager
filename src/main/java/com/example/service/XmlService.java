package com.example.service;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.example.model.XMLField;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class XmlService {
    public List<XMLField> parseXmlFile(String filePath) {
        List<XMLField> xmlFields = new ArrayList<>();
   
        try {
            String processedXml = preProcessXml(new FileInputStream(filePath));
            InputStream xmlStream = new ByteArrayInputStream(processedXml.getBytes(StandardCharsets.UTF_8));
   
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlStream);
            doc.getDocumentElement().normalize();
   
            NodeList docReferenceList = doc.getElementsByTagName("BusinessTransactionDocumentReference");
            for (int i = 0; i < docReferenceList.getLength(); i++) {
                Element element = (Element) docReferenceList.item(i);
                String typeCode = getTagValue("TypeCode", element);
                String idValue = getTagValue("ID", element);
   
                List<String[]> mappings = extractStructure(element, "BusinessTransactionDocumentReference");
                for (String[] mappingPair : mappings) {
                    xmlFields.add(new XMLField("<TypeCode>" + typeCode + "</TypeCode>", mappingPair[0], mappingPair[1])); // Separate path and value
                }
            }
   
            NodeList additionalPartyList = doc.getElementsByTagName("AdditionalParty");
            for (int i = 0; i < additionalPartyList.getLength(); i++) {
                Element element = (Element) additionalPartyList.item(i);
                String roleCode = getTagValue("RoleCode", element);
   
                List<String[]> mappings = extractStructure(element, "AdditionalParty");
                for (String[] mappingPair : mappings) {
                    xmlFields.add(new XMLField("<RoleCode>" + roleCode + "</RoleCode>", mappingPair[0], mappingPair[1]));
                }
            }
   
            NodeList additionalDocumentList = doc.getElementsByTagName("AdditionalDocument");
            for (int i = 0; i < additionalDocumentList.getLength(); i++) {
                Element element = (Element) additionalDocumentList.item(i);
                String typeCode = getTagValue("TypeCode", element);
   
                List<String[]> mappings = extractStructure(element, "AdditionalDocument");
                for (String[] mappingPair : mappings) {
                    xmlFields.add(new XMLField("<TypeCode>" + typeCode + "</TypeCode>", mappingPair[0], mappingPair[1]));
                }
            }
   
            NodeList textList = doc.getElementsByTagName("Text");
            for (int i = 0; i < textList.getLength(); i++) {
                Element element = (Element) textList.item(i);
                String typeCode = getTagValue("TypeCode", element);
   
                List<String[]> mappings = extractStructure(element, "Text");
                for (String[] mappingPair : mappings) {
                    xmlFields.add(new XMLField("<TypeCode>" + typeCode + "</TypeCode>", mappingPair[0], mappingPair[1]));
                }
            }
   
            NodeList packageList = doc.getElementsByTagName("Package");
            for (int i = 0; i < packageList.getLength(); i++) {
                Element element = (Element) packageList.item(i);
                String typeCode = getTagValue("TypeCode", element);
   
                List<String[]> mappings = extractStructure(element, "Package");
                for (String[] mappingPair : mappings) {
                    xmlFields.add(new XMLField("<TypeCode>" + typeCode + "</TypeCode>", mappingPair[0], mappingPair[1]));
                }
            }
   
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlFields;
    }
   


    public void exportToExcel(HttpServletResponse response, List<XMLField> xmlFields, String fileNameWithoutExtension) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("XML Data");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            createHeaderCell(workbook, headerRow, 0, "XML Field ID");
            createHeaderCell(workbook, headerRow, 1, "XML Field Mapping");
            createHeaderCell(workbook, headerRow, 2, "Value");
            
            int rowNum = 1;  // Start from row 1 as 0 is the header row
            
            // Track the starting row for merging cells
            String lastId = "";
            int startRow = 1;
            
            CellStyle wrapTextStyle = workbook.createCellStyle();
            wrapTextStyle.setWrapText(true);
            
            for (XMLField xmlField : xmlFields) {
                Row row = sheet.createRow(rowNum);
                row.createCell(1).setCellValue(xmlField.getMapping());
                row.createCell(2).setCellValue(xmlField.getValue());
                
                if (!xmlField.getId().equals(lastId)) {
                    // Merge cells for previous XML Field ID if needed
                    if (!lastId.isEmpty() && rowNum > startRow) {
                        sheet.addMergedRegion(new CellRangeAddress(startRow, rowNum - 1, 0, 0));
                    }
                    
                    // Set XML Field ID in the first cell for the new field
                    Cell idCell = row.createCell(0);
                    idCell.setCellValue(xmlField.getId());
                    idCell.setCellStyle(wrapTextStyle);
                    
                    // Update tracking variables
                    lastId = xmlField.getId();
                    startRow = rowNum;
                }
                
                rowNum++;
            }
            
            // Merge cells for the last XML Field ID if needed
            if (rowNum > startRow) {
                sheet.addMergedRegion(new CellRangeAddress(startRow, rowNum - 1, 0, 0));
            }
            
            
            // Set column widths for readability
            sheet.setColumnWidth(0, 8000);  // ID column width
            
            
            sheet.setColumnWidth(2, 8000);  // Value column width
            sheet.autoSizeColumn(1);
            
            
            // Set the content type and headers for the response
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileNameWithoutExtension + ".xlsx");
            
            // Write workbook to response output stream
            workbook.write(response.getOutputStream());
        }
    }
   
   


    // Helper method to create a styled header cell
    private void createHeaderCell(Workbook workbook, Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);


        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // Center-align the header
        cell.setCellStyle(headerStyle);
    }






    private String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent().trim();
        }
        return "";
    }


    private String preProcessXml(InputStream inputStream) throws Exception {
        byte[] xmlBytes = inputStream.readAllBytes();
        String xmlContent = new String(xmlBytes, StandardCharsets.UTF_8);


        String[] lines = xmlContent.split("\n");
        if (lines[0].contains("This XML file does not appear to have any style information")) {
            xmlContent = String.join("\n", java.util.Arrays.copyOfRange(lines, 1, lines.length));
        }


        xmlContent = xmlContent.replaceAll("&(?!amp;|lt;|gt;|quot;|apos;)", "&amp;");
        if (!xmlContent.trim().startsWith("<Root>")) {
            xmlContent = "<Root>\n" + xmlContent + "\n</Root>";
        }


        return xmlContent;
    }


    private List<String[]> extractStructure(Element element, String parentTag) {
        List<String[]> mappings = new ArrayList<>();
        extractNestedStructure(element, parentTag, new ArrayList<>(), mappings);
        return mappings;
    }
   
    private void extractNestedStructure(Node node, String path, List<String> pathStack, List<String[]> mappings) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            pathStack.add(element.getNodeName());
   
            NodeList children = element.getChildNodes();
            boolean hasTextContent = false;
   
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    extractNestedStructure(child, path, pathStack, mappings);
                } else if (child.getNodeType() == Node.TEXT_NODE && !child.getTextContent().trim().isEmpty()) {
                    hasTextContent = true;
                    String pathStr = String.join(" -> ", pathStack);
                    String valueStr = child.getTextContent().trim();
                    mappings.add(new String[]{pathStr, valueStr}); // Store path and value as a pair
                }
            }
            if (hasTextContent) {
                pathStack.remove(pathStack.size() - 1);
            }
        }
    }
   


}
