package com.example.demo.controller;

import com.example.demo.entity.ExcelData;
import com.example.demo.service.ExcelDataService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExcelReaderController {

    private static final String EXCEL_FILE_PATH = "C:\\Users\\Admin1\\OneDrive\\Desktop\\hello.xlsx";

    @Autowired
    private ExcelDataService excelDataService;

    @GetMapping("/readExcel")
    public ResponseEntity<List<List<String>>> readExcel() {
        try {
            List<List<String>> excelData = readExcelFile();
            return ResponseEntity.ok(excelData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/addDataToExcel")
    public ResponseEntity<String> addDataToExcel(@RequestBody List<List<String>> newData) {
        try {
            List<List<String>> excelData = readExcelFile();
            excelData.addAll(newData);
            writeExcelFile(excelData);
            return ResponseEntity.ok("Data added to Excel file successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateDataInExcel/{rowIndex}")
    public ResponseEntity<String> updateDataInExcel(@PathVariable int rowIndex, @RequestBody List<String> updatedData) {
        try {
            List<List<String>> excelData = readExcelFile();
            if (rowIndex > 0 && rowIndex <= excelData.size()) {
                excelData.set(rowIndex - 1, updatedData);
                writeExcelFile(excelData);
                return ResponseEntity.ok("Data updated successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteDataFromExcel/{rowIndex}")
    public ResponseEntity<String> deleteDataFromExcel(@PathVariable int rowIndex) {
        try {
            List<List<String>> excelData = readExcelFile();
            if (rowIndex > 0 && rowIndex <= excelData.size()) {
                excelData.remove(rowIndex - 1);
                writeExcelFile(excelData);
                return ResponseEntity.ok("Data deleted successfully");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/readExcelAndStoreToDatabase")
    public ResponseEntity<String> readExcelAndStoreToDatabase() {
        try {
            List<ExcelData> excelDataList = readExcelFileAndMapToEntities();
            excelDataService.saveExcelData(excelDataList);
            return ResponseEntity.ok("Excel data stored in the database successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while reading Excel file and storing data in the database");
        }
    }

    @GetMapping("/syncExcelWithDatabase")
    public ResponseEntity<String> syncExcelWithDatabase() {
        try {
            List<ExcelData> excelDataList = readExcelFileAndMapToEntities();
            excelDataService.syncExcelDataWithDatabase(excelDataList);
            return ResponseEntity.ok("Excel data synced with the database successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while syncing Excel data with the database");
        }
    }

    @DeleteMapping("/deleteDataFromDatabaseAndExcel/{rowIndex}")
    public ResponseEntity<String> deleteDataFromDatabaseAndExcel(@PathVariable int rowIndex) {
        try {
            List<List<String>> excelData = readExcelFile();
            if (rowIndex > 0 && rowIndex <= excelData.size()) {
                ExcelData existingData = excelDataService.getExcelDataByCandidateName(excelData.get(rowIndex - 1).get(0));
                if (existingData != null) {
                    excelDataService.deleteExcelData(existingData);
                    excelData.remove(rowIndex - 1);
                    writeExcelFile(excelData);
                    return ResponseEntity.ok("Data deleted successfully from database and Excel file");
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateDataInDatabaseAndExcel/{rowIndex}")
    public ResponseEntity<String> updateDataInDatabaseAndExcel(@PathVariable int rowIndex, @RequestBody ExcelData updatedData) {
        try {
            List<List<String>> excelData = readExcelFile();
            if (rowIndex > 0 && rowIndex <= excelData.size()) {
                ExcelData existingData = excelDataService.getExcelDataByCandidateName(excelData.get(rowIndex - 1).get(0));
                if (existingData != null) {
                    updatedData.setId(existingData.getId());
                    excelDataService.updateExcelData(updatedData);

                    // Update the row in the excelData list
                    List<String> updatedRow = mapEntityToRow(updatedData);
                    excelData.set(rowIndex - 1, updatedRow);

                    // Write the updated excelData list to the Excel file
                    writeExcelFile(excelData);

                    return ResponseEntity.ok("Data updated successfully in database and Excel file");
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    private List<List<String>> readExcelFile() throws IOException {
        List<List<String>> excelData = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(new File(EXCEL_FILE_PATH));
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
                if (cell == null) {
                    rowData.add("");
                } else {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        default:
                            rowData.add("");
                    }
                }
            }
            excelData.add(rowData);
        }
        workbook.close();
        inputStream.close();
        return excelData;
    }
  

    private List<ExcelData> readExcelFileAndMapToEntities() throws IOException {
        List<ExcelData> excelDataList = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(new File(EXCEL_FILE_PATH));
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
        excelData.setPhone_number(getStringValue(row.getCell(2)));
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


    private List<String> mapEntityToRow(ExcelData excelData) {
        List<String> rowData = new ArrayList<>();
        rowData.add(excelData.getCandidate_name());
        rowData.add(excelData.getJoining_month());
        rowData.add(excelData.getPhone_number());
        rowData.add(excelData.getReference());
        rowData.add(excelData.getBp_name());
        rowData.add(excelData.getCommunication());
        rowData.add(excelData.getTechnical());
        rowData.add(excelData.getStatus());
        rowData.add(excelData.getScholarship());
        rowData.add(excelData.getEnquired_by());
        return rowData;
    }

    private void writeExcelFile(List<List<String>> data) throws IOException {
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
        FileOutputStream outputStream = new FileOutputStream(EXCEL_FILE_PATH);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}