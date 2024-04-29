package com.example.demo.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.ExcelData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelFileUtils {

    @Value("classpath:hello.xlsx")
    private Resource excelFileResource;

    @SuppressWarnings("unused")
    public List<List<String>> readExcelFile(MultipartFile file) throws IOException {
        List<List<String>> excelData = new ArrayList<>();
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                if (cell == null) {
                    rowData.add("");  // Add an empty string for null cells
                } else if (cell.getCellType() == CellType.STRING) {
                    rowData.add(cell.getStringCellValue());
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    // Convert numeric value to string
                    rowData.add(String.valueOf(cell.getNumericCellValue()));
                } else {
                    rowData.add("");  // Add an empty string for other cell types
                }
            }
            excelData.add(rowData);
        }
        workbook.close();
        inputStream.close();
        return excelData;
    }

    public List<List<String>> readExcelFileFromResource() throws IOException {
        List<List<String>> excelData = new ArrayList<>();
        InputStream inputStream = excelFileResource.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                if (cell == null) {
                    rowData.add("");  // Add an empty string for null cells
                } else if (cell.getCellType() == CellType.STRING) {
                    rowData.add(cell.getStringCellValue());
                } else if (cell.getCellType() == CellType.NUMERIC) {
                    // Convert numeric value to string
                    rowData.add(String.valueOf(cell.getNumericCellValue()));
                } else {
                    rowData.add("");  // Add an empty string for other cell types
                }
            }
            excelData.add(rowData);
        }
        workbook.close();
        inputStream.close();
        return excelData;
    }

    public void writeExcelFile(List<List<String>> data, String fileName) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        for (int i = 0; i < data.size(); i++) {
            Row row = sheet.createRow(i);
            List<String> rowData = data.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData.get(j));
            }
        }
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public List<ExcelData> readExcelFileAndMapToEntities(MultipartFile file) throws IOException {
        List<ExcelData> excelDataList = new ArrayList<>();
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (row.getRowNum() == 0) { // Skip header row
                continue;
            }
            ExcelData excelData = mapRowToEntity(row);
            excelDataList.add(excelData);
        }
        workbook.close();
        inputStream.close();
        return excelDataList;
    }
    
    private ExcelData mapRowToEntity(Row row) {
        ExcelData excelData = new ExcelData();
        excelData.setCandidate_name(getStringValue(row.getCell(0)));
        excelData.setJoining_month(getStringValue(row.getCell(1)));
        excelData.setPhone_number(getStringValue(row.getCell(2))); // Changed to handle as string
        excelData.setReference(getStringValue(row.getCell(3)));
        excelData.setBp_name(getStringValue(row.getCell(4)));
        excelData.setCommunication(getStringValue(row.getCell(5)));
        excelData.setTechnical(getStringValue(row.getCell(6)));
        excelData.setStatus(getStringValue(row.getCell(7)));
        excelData.setScholarship(getStringValue(row.getCell(8)));
        excelData.setEnquired_by(getStringValue(row.getCell(9)));
        return excelData;
    }

    private String getStringValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK){
            return "NF";  // Return null for empty cells
        } else if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            // Convert numeric value to string without scientific notation
            return String.valueOf((long) cell.getNumericCellValue());
        } else {
            return null;  // Handle other cell types if needed
        }
    }


    // Other methods remain the same
}