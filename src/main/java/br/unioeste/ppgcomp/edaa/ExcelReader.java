package br.unioeste.ppgcomp.edaa;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {
    public static void calculateAndSaveAverage(String inputFileName, String dataType) {
        try {
            FileInputStream fileIn = new FileInputStream(inputFileName);
            Workbook inputWorkbook = new XSSFWorkbook(fileIn);
            Sheet inputSheet = inputWorkbook.getSheetAt(0);

            FileInputStream fileOutAVG = new FileInputStream("C:/SortEDAA/dataAVG.xlsx");
            Workbook outputWorkbook = new XSSFWorkbook(fileOutAVG);
            Sheet outputSheet = outputWorkbook.getSheetAt(0);

            Iterator<Row> rowIterator = inputSheet.iterator();
            rowIterator.next(); // Skip the header row

            List<Integer> executionTimes = new ArrayList<>();

            Row outputRow = outputSheet.createRow(outputSheet.getLastRowNum() + 1);

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                executionTimes.add((int) row.getCell(2).getNumericCellValue());

                outputRow.createCell(0).setCellValue(row.getCell(0).getStringCellValue());
                outputRow.createCell(1).setCellValue(dataType);
                outputRow.createCell(2).setCellValue(row.getCell(1).getNumericCellValue());
                outputRow.createCell(4).setCellValue(row.getCell(3).getNumericCellValue());
            }

            // Remove the minimum and maximum values
            Collections.sort(executionTimes);
            executionTimes.remove(executionTimes.size() - 1);
            executionTimes.remove(0);

            // Calculate the average
            int avgExecutionTime = executionTimes.stream().mapToInt(val -> val).sum() / executionTimes.size();

            outputRow.createCell(3).setCellValue(avgExecutionTime);

            fileIn.close();
            inputWorkbook.close();
            fileOutAVG.close();

            FileOutputStream fileOut = new FileOutputStream("C:/SortEDAA/dataAVG.xlsx");
            outputWorkbook.write(fileOut);
            fileOut.close();
            outputWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}