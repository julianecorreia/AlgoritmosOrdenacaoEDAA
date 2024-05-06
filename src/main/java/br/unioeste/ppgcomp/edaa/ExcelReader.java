package br.unioeste.ppgcomp.edaa;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelReader {
    public static void calculateAndSaveAverage(String inputFileName) {
        try {
            FileInputStream fileIn = new FileInputStream(inputFileName);
            Workbook inputWorkbook = new XSSFWorkbook(fileIn);
            Sheet inputSheet = inputWorkbook.getSheetAt(0);

            FileInputStream fileOutAVG = new FileInputStream("C:/EDAA/dataAVG.xlsx");
            Workbook outputWorkbook = new XSSFWorkbook(fileOutAVG);
            Sheet outputSheet = outputWorkbook.getSheetAt(0);

            Iterator<Row> rowIterator = inputSheet.iterator();
            rowIterator.next(); // Skip the header row

            int sumExecutionTime = 0;
            int sumMemoryUsage = 0;
            int sumComparisons = 0;
            int rowCount = 0;

            double avgExecutionTime = 0;
            double avgMemoryUsage = 0;
            double avgComparisons = 0;

            Row outputRow = outputSheet.createRow(outputSheet.getLastRowNum() + 1);

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                sumExecutionTime += row.getCell(2).getNumericCellValue();
                sumMemoryUsage += row.getCell(3).getNumericCellValue();
                sumComparisons += row.getCell(4).getNumericCellValue();
                rowCount++;

                outputRow.createCell(0).setCellValue(row.getCell(0).getStringCellValue());
                outputRow.createCell(1).setCellValue(row.getCell(1).getNumericCellValue());

                 avgExecutionTime = sumExecutionTime / rowCount;
                 avgMemoryUsage = sumMemoryUsage / rowCount;
                 avgComparisons = sumComparisons / rowCount;
            }

            outputRow.createCell(2).setCellValue(avgExecutionTime);
            outputRow.createCell(3).setCellValue(avgMemoryUsage);
            outputRow.createCell(4).setCellValue(avgComparisons);

            sumExecutionTime = 0;
            sumMemoryUsage = 0;
            sumComparisons = 0;
            rowCount = 0;

            fileIn.close();
            inputWorkbook.close();
            fileOutAVG.close();

            FileOutputStream fileOut = new FileOutputStream("C:/EDAA/dataAVG.xlsx");
            outputWorkbook.write(fileOut);
            fileOut.close();
            outputWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}