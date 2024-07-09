package br.unioeste.ppgcomp.edaa;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Main {

    private static final int ONE_HUNDRED = 100;
    private static final int TWO_HUNDRED = 200;
    private static final int FIVE_HUNDRED = 500;
    private static final int ONE_THOUSAND = 1000;
    private static final int TWO_THOUSAND = 2000;
    private static final int FIVE_THOUSAND = 5000;
    private static final int SEVEN_HALF_THOUSAND = 7500;
    private static final int TEN_THOUSAND = 10000;
    private static final int FIFTEEN_THOUSAND = 15000;
    private static final int THIRTY_THOUSAND = 30000;
    private static final int FIFTY_THOUSAND = 50000;
    private static final int SEVENTY_FIVE_THOUSAND = 75000;
    private static final int ONE_HUNDRED_THOUSAND = 100000;
    private static final int TWO_HUNDRED_THOUSAND = 200000;
    private static final int FIVE_HUNDRED_THOUSAND = 500000;
    private static final int SEVEN_HUNDRED_FIFTY_THOUSAND = 750000;
    private static final int ONE_MILLION = 1000000;
    private static final int ONE_MILLION_TWO_HUNDRED_FIFTY_THOUSAND = 1250000;
    private static final int ONE_MILLION_FIVE_HUNDRED_THOUSAND = 1500000;
    private static final int TWO_MILLION = 2000000;

    private static int[] sizes = {ONE_HUNDRED, TWO_HUNDRED, FIVE_HUNDRED, ONE_THOUSAND, TWO_THOUSAND, FIVE_THOUSAND,
            SEVEN_HALF_THOUSAND, TEN_THOUSAND, FIFTEEN_THOUSAND, THIRTY_THOUSAND, FIFTY_THOUSAND, SEVENTY_FIVE_THOUSAND,
            ONE_HUNDRED_THOUSAND, TWO_HUNDRED_THOUSAND, FIVE_HUNDRED_THOUSAND, SEVEN_HUNDRED_FIFTY_THOUSAND, ONE_MILLION,
            ONE_MILLION_TWO_HUNDRED_FIFTY_THOUSAND, ONE_MILLION_FIVE_HUNDRED_THOUSAND, TWO_MILLION};

    private static DataType[] dataTypes = {DataType.RAND, DataType.DESC, DataType.SORT, DataType.HALF_SORT};
    private static SortType[] sortTypes = {SortType.QUICK, SortType.MERGE, SortType.HEAP};
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        for (DataType dataType : dataTypes) {
            for (int size : sizes) {
                for (SortType sortType : sortTypes) {
                    createSheet(size, sortType, dataType);
                }
            }
        }

        for (DataType dataType : dataTypes) {
            for (int size : sizes) {
                for (int i = 0; i < 7; i++) {
                    System.out.println(STR."Lap: \{i + 1}");

                    quickSortTest(size, SortType.QUICK.getName(), dataType);
                    mergeSortTest(size, SortType.MERGE.getName(), dataType);
                    heapSortTest(size, SortType.HEAP.getName(), dataType);
                }
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println(STR."Total execution time: \{endTime - startTime} milliseconds");

        average();
    }

    private static void heapSortTest(int size, String sortType, DataType dataType) {
        //cria um array de tamanho size
        int[] array = new int[size];

        //preenche o array com os valores do arquivo
        fillArrayFromFile(array, STR."C:/SortEDAA/\{dataType.getFolder()}\{size}.txt");

        for (int element : array) {
            System.out.print(STR."\{element}, ");
        }

        long startTime = System.nanoTime();

        int result = heapSort(array);

        printExecutionTime(startTime, result, sortType, size, dataType.getName());

//        for (int element : array) {
//            System.out.print(STR."\{element}, ");
//        }
    }

    private static int heapSort(int[] array) {
        int n = array.length;
        int comparisons = 0;

        // Construa o heap (reorganize o array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            comparisons += heapify(array, n, i);
        }

        // Um por um, extraia um elemento do heap
        for (int i = n - 1; i >= 0; i--) {
            // Mova o atual root para o fim
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // Chame heapify no heap reduzido
            comparisons += heapify(array, i, 0);
        }

        return comparisons;
    }

    private static int heapify(int[] array, int n, int i) {
        int comparisons = 0;

        while (true) {
            int largest = i; // Inicialize o maior como root
            int left = 2 * i + 1; // esquerda = 2*i + 1
            int right = 2 * i + 2; // direita = 2*i + 2

            // Se o filho da esquerda é maior que o root
            if (left < n) {
                comparisons++;
                if (array[left] > array[largest]) {
                    largest = left;
                }
            }

            // Se o filho da direita é maior que o maior até agora
            if (right < n) {
                comparisons++;
                if (array[right] > array[largest]) {
                    largest = right;
                }
            }

            // Se o maior não é o root
            if (largest != i) {
                int swap = array[i];
                array[i] = array[largest];
                array[largest] = swap;

                // Mova o root para o maior para continuar o heapify
                i = largest;
            } else {
                break;
            }
        }

        return comparisons;
    }

    private static void average() {
        long startTime = System.currentTimeMillis();
        createSheetAVG();
        for (int size : sizes) {
            for (SortType sortType : sortTypes) {
                for (DataType dataType : dataTypes) {
                    String fileName = STR."C:/SortEDAA/data_\{size}_\{sortType.getName()}_\{dataType.getName()}.xlsx";
                    ExcelReader.calculateAndSaveAverage(fileName, dataType.getName());
                }
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(STR."Total execution time: \{endTime - startTime} milliseconds");
    }



    private static void mergeSortTest(int size, String sortType, DataType dataType) {
        //cria um array de tamanho size
        int[] array = new int[size];

        //preenche o array com os valores do arquivo
        fillArrayFromFile(array, STR."C:/SortEDAA/\{dataType.getFolder()}\{size}.txt");

//        for (int element : array) {
//            System.out.print(STR."\{element}, ");
//        }

        long startTime = System.nanoTime();

        int result = mergeSort(array, 0, array.length - 1);

        printExecutionTime(startTime, result, sortType, size, dataType.getName());

//        for (int element : array) {
//            System.out.print(STR."\{element}, ");
//        }
    }

    private static int mergeSort(int[] array, int left, int right) {
        int comparisons = 0;
        int n = array.length;

        for (int size = 1; size < n; size = 2 * size) {
            for (int leftStart = 0; leftStart < n - 1; leftStart += 2 * size) {
                int mid = Math.min(leftStart + size - 1, n - 1);
                int rightEnd = Math.min(leftStart + 2 * size - 1, n - 1);

                comparisons += merge(array, leftStart, mid, rightEnd);
            }
        }

        return comparisons;
    }

    private static int merge(int[] array, int left, int mid, int right) {
        int[] leftArray = new int[mid - left + 1];
        int[] rightArray = new int[right - mid];

        System.arraycopy(array, left, leftArray, 0, mid - left + 1);
        System.arraycopy(array, mid + 1, rightArray, 0, right - mid);

        int i = 0, j = 0;
        int k = left;
        int comparisons = 0;

        while (i < leftArray.length && j < rightArray.length) {
            comparisons++;
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < leftArray.length) {
            array[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < rightArray.length) {
            array[k] = rightArray[j];
            j++;
            k++;
        }

        return comparisons;
    }

    /**
     * Este método executa o algoritmo QuickSort em um array de inteiros.
     * @param size Tamanho do array.
     * @param sortType Tipo de ordenação.
     * @param dataType Tipo de dado.
     */
    private static void quickSortTest(int size, String sortType, DataType dataType) {
        //cria um array de tamanho size
        int[] array = new int[size];

        //preenche o array com os valores do arquivo
        fillArrayFromFile(array, STR."C:/SortEDAA/\{dataType.getFolder()}\{size}.txt");

//        for (int element : array) {
//            System.out.print(STR."\{element}, ");
//        }

        //começa a contar o tempo
        long startTime = System.nanoTime();

        int result = quickSort(array);

        printExecutionTime(startTime, result, sortType, size, dataType.getName());

//        for (int element : array) {
//            System.out.print(STR."\{element}, ");
//        }
    }

    private static int quickSort(int[] array) {
        int comparisons = 0;
        int[] stack = new int[array.length * 2];

        int top = -1;
        stack[++top] = 0;
        stack[++top] = array.length - 1;

        while (top >= 0) {
            int high = stack[top--];
            int low = stack[top--];

            int[] partitionResult = partition(array, low, high);
            int pivot = partitionResult[0];
            comparisons += partitionResult[1];

            if (pivot - 1 > low) {
                stack[++top] = low;
                stack[++top] = pivot - 1;
            }

            if (pivot + 1 < high) {
                stack[++top] = pivot + 1;
                stack[++top] = high;
            }
        }

        return comparisons;
    }

    private static int[] partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        int comparisons = 0;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;

                if (i != j) {
                    comparisons++;
                }

                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        if (i + 1 != high) {
            comparisons++;
        }

        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return new int[]{i + 1, comparisons};
    }

    private static void fillArrayFromFile(int[] array, String fileName) {
        long startTime = System.currentTimeMillis();

        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            int index = 0;
            while (scanner.hasNextInt() && index < array.length) {
                array[index++] = scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            System.out.println(STR."File not found: \{fileName}");
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(STR."Execution time fillArrayFromFile \{fileName}: \{endTime - startTime} milliseconds");
    }


    private static void createSheet(int size, SortType sortType, DataType dataType) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(STR."Data \{size} - \{sortType.getName()} - \{dataType.getName()}");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Sort Type");
            header.createCell(1).setCellValue("Size");
            header.createCell(2).setCellValue("Execution Time (nanoseconds)");
            header.createCell(3).setCellValue("Comparisons");

            FileOutputStream fileOut = new FileOutputStream(STR."C:/SortEDAA/data_\{size}_\{sortType.getName()}_\{dataType.getName()}.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método cria uma planilha Excel com os resultados médios dos testes.
     */
    private static void createSheetAVG() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(STR."DataAVG");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Search Type");
            header.createCell(1).setCellValue("Data Type");
            header.createCell(2).setCellValue("Size");
            header.createCell(3).setCellValue("AVG Execution Time (nanoseconds)");
            header.createCell(4).setCellValue("Comparisons");

            FileOutputStream fileOut = new FileOutputStream(STR."C:/SortEDAA/dataAVG.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private static void printExecutionTime(long startTime, int comparisons,
                                           String sortType, int size, String dataType) {
        //finaliza a contagem do tempo
        long endTime = System.nanoTime();

        System.out.println(STR." ");
        System.out.println(STR."\{sortType} \{size} - execution time: \{endTime - startTime} nanoseconds");
        System.out.println(STR."\{sortType} \{size} - comparisons: \{comparisons} x");

        System.out.println(STR." ");
        System.out.println(STR."---------------------------------------------------------------------------------");

        try {
            FileInputStream fileIn = new FileInputStream(STR."C:/SortEDAA/data_\{size}_\{sortType}_\{dataType}.xlsx");
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            int lastRowNum = sheet.getLastRowNum();
            Row dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(sortType);
            dataRow.createCell(1).setCellValue(size);
            dataRow.createCell(2).setCellValue(endTime - startTime);
            dataRow.createCell(3).setCellValue(comparisons);

            fileIn.close();

            FileOutputStream fileOut = new FileOutputStream(STR."C:/SortEDAA/data_\{size}_\{sortType}_\{dataType}.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}