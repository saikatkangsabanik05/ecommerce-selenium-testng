package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelDataProvider - Reads test data from Excel (.xlsx) files
 * Use with @DataProvider in TestNG test classes
 */
public class ExcelDataProvider {

    private static final Logger log = LogManager.getLogger(ExcelDataProvider.class);
    private static final String TEST_DATA_PATH = "test-data/";

    /**
     * Returns test data as Object[][] for TestNG @DataProvider
     */
    public static Object[][] getTestData(String fileName, String sheetName) {
        List<Map<String, String>> dataList = readExcel(fileName, sheetName);
        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }

    public static List<Map<String, String>> readExcel(String fileName, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        String filePath = TEST_DATA_PATH + fileName;

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                log.error("Sheet '{}' not found in '{}'", sheetName, filePath);
                return dataList;
            }

            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();
            List<String> headers = new ArrayList<>();
            for (int i = 0; i < colCount; i++) {
                headers.add(getCellValue(headerRow.getCell(i)));
            }

            for (int rowIdx = 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
                Row row = sheet.getRow(rowIdx);
                if (row == null) continue;
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int colIdx = 0; colIdx < colCount; colIdx++) {
                    rowData.put(headers.get(colIdx), getCellValue(row.getCell(colIdx)));
                }
                dataList.add(rowData);
            }

            log.info("Loaded {} rows from sheet '{}' in '{}'", dataList.size(), sheetName, filePath);

        } catch (IOException e) {
            log.error("Error reading Excel file {}: {}", filePath, e.getMessage());
        }

        return dataList;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default:      return "";
        }
    }
}
