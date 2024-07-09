package br.unioeste.ppgcomp.edaa;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Main {

    private static final int ONE_HUNDRED_THOUSAND = 100000;
    private static final int TWO_HUNDRED_THOUSAND = 200000;
    private static final int THREE_HUNDRED_THOUSAND = 300000;
    private static final int FOUR_HUNDRED_THOUSAND = 400000;
    private static final int FIVE_HUNDRED_THOUSAND = 500000;
    private static final int SIX_HUNDRED_THOUSAND = 600000;
    private static final int SEVEN_HUNDRED_THOUSAND = 700000;
    private static final int EIGHT_HUNDRED_THOUSAND = 800000;
    private static final int NINE_HUNDRED_THOUSAND = 900000;
    private static final int ONE_MILLION = 1000000;

    private static int[] constants = {ONE_HUNDRED_THOUSAND, TWO_HUNDRED_THOUSAND, THREE_HUNDRED_THOUSAND,
            FOUR_HUNDRED_THOUSAND, FIVE_HUNDRED_THOUSAND, SIX_HUNDRED_THOUSAND, SEVEN_HUNDRED_THOUSAND,
            EIGHT_HUNDRED_THOUSAND, NINE_HUNDRED_THOUSAND, ONE_MILLION};

    private static String[] searchTypes = {"sequentialTest", "binarySearchTest",
            "sequentialLinkedListTest", "binarySearchBinaryTreeTest"};


    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        //createFiles();

        long startTime = System.currentTimeMillis();

        for (int constant : constants) {
            createSheet(constant, "sequentialTest");
            createSheet(constant, "binarySearchTest");
            createSheet(constant, "sequentialLinkedListTest");
            createSheet(constant, "binarySearchBinaryTreeTest");


            int[] arraySequential = new int[constant];
            fillArrayFromFile(arraySequential, STR."C:/EDAA/randomElements_\{constant}.txt");

            int[] arrayBinary = new int[constant];
            fillArrayFromFile(arrayBinary, STR."C:/EDAA/randomElements_\{constant}.txt");
            Arrays.sort(arrayBinary);

            LinkedList list = new LinkedList();
            fillListFromFile(list, STR."C:/EDAA/randomElements_\{constant}.txt");

            BinaryTree tree = new BinaryTree();
            fillTreeFromFile(tree, STR."C:/EDAA/randomElements_\{constant}.txt");

            for (int i = 0; i < 100; i++) {
                System.out.println(STR."Lap: \{i + 1}");

                int target = new Random().nextInt(constant) + 1;
                sequentialTest(arraySequential, constant, target);
                binarySearchTest(arrayBinary, constant, target);
                sequentialLinkedListTest(list, constant, target);
                binarySearchBinaryTreeTest(tree, constant, target);
            }
        }

//
//        //pior caso //está comentado para rodar separado
//        for (int constant : constants) {
//            createSheet(constant, "sequentialTest");
//            createSheet(constant, "binarySearchTest");
//            createSheet(constant, "sequentialLinkedListTest");
//            createSheet(constant, "binarySearchBinaryTreeTest");
//
//
//            int[] arraySequential = new int[constant];
//            fillArrayFromFile(arraySequential, STR."C:/EDAA/randomElements_\{constant}.txt");
//
//            int[] arrayBinary = new int[constant];
//            fillArrayFromFile(arrayBinary, STR."C:/EDAA/randomElements_\{constant}.txt");
//            Arrays.sort(arrayBinary);
//
//            LinkedList list = new LinkedList();
//            fillListFromFile(list, STR."C:/EDAA/randomElements_\{constant}.txt");
//
//            BinaryTree tree = new BinaryTree();
//            fillTreeFromFile(tree, STR."C:/EDAA/randomElements_\{constant}.txt");
//
//            int targetTree = tree.getHeight();
//
//            for (int i = 0; i < 3; i++) {
//                System.out.println(STR."Lap: \{i + 1}");
//
//                int target = -1;
//                sequentialTest(arraySequential, constant, target);
//                binarySearchTest(arrayBinary, constant, target);
//                sequentialLinkedListTest(list, constant, target);
//
//                binarySearchBinaryTreeTest(tree, constant, targetTree);
//            }
//        }
//

        long endTime = System.currentTimeMillis();
        System.out.println(STR."Total execution time: \{endTime - startTime} milliseconds");

        average();
    }

    private static void average() {
        long startTime = System.currentTimeMillis();
        createSheetAVG();
        for (int constant : constants) {
            for (String searchType : searchTypes) {
                String fileName = STR."C:/EDAA/data\{constant}_\{searchType}.xlsx";
                ExcelReader.calculateAndSaveAverage(fileName);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(STR."Total execution time: \{endTime - startTime} milliseconds");
    }

    /**
     * Este método realiza uma busca binária em uma árvore binária.
     *
     * @param size O tamanho da árvore binária.
     * @param target O alvo da busca.
     */
    private static void binarySearchBinaryTreeTest(BinaryTree tree, int size, int target) {
        long sumComparisons = 0, sumExecutionTime = 0, sumMemoryUsage = 0;
        long startTime = System.nanoTime();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        SearchResult result = binarySearchBinaryTree(tree, target);

        printExecutionTime(startTime, beforeUsedMem, result.getComparisons(), STR."binarySearchBinaryTreeTest", size, result.getIndex(), target);
    }
    /**
     * Este método realiza uma busca binária em uma árvore binária.
     * Foi separado somente a busca para facilitar as medidas dos testes.
     *
     * @param tree A árvore binária na qual a busca será realizada.
     * @param target O alvo da busca.
     * @return Retorna o nó que contém o alvo se ele for encontrado, caso contrário, retorna null.
     */
    public static SearchResult binarySearchBinaryTree(BinaryTree tree, int target) {
        Node current = tree.root;
        int comparisons = 0;

        while (current != null) {
            comparisons++;
            if (current.getValue() == target) {
                return new SearchResult(current.getValue(), comparisons);
            }

            if (target < current.getValue()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        return new SearchResult(-1, comparisons);
    }

    /**
     * Este método realiza uma busca sequencial em uma lista encadeada.
     *
     * @param size O tamanho da lista encadeada.
     * @param target O alvo da busca.
     */
    private static void sequentialLinkedListTest(LinkedList list, int size, int target) {
        long sumComparisons = 0, sumExecutionTime = 0, sumMemoryUsage = 0;
        long startTime = System.nanoTime();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        SearchResult result = sequentialSearchLinkedList(list, target);

        printExecutionTime(startTime, beforeUsedMem, result.getComparisons(), STR."sequentialLinkedListTest", size, result.getIndex(), target);
    }
    /**
     * Este método realiza uma busca sequencial em uma lista encadeada.
     * Foi separado somente a busca para facilitar as medidas dos testes.
     *
     * @param list A lista encadeada na qual a busca será realizada.
     * @param target O alvo da busca.
     * @return Retorna o índice do alvo se ele for encontrado, caso contrário, retorna -1.
     */
    public static SearchResult sequentialSearchLinkedList(LinkedList list, int target) {
        int comparisons = 0;
        Node current = list.head;
        while (current != null) {
            comparisons++;
            if (current.getValue() == target) {
                return new SearchResult(comparisons, comparisons);
            }
            current = current.getRight();
        }
        return new SearchResult(-1, comparisons);
    }

    /**
     * Este método realiza uma busca binária em um array.
     *
     * @param size O tamanho do array.
     * @param target O alvo da busca.
     * @throws FileNotFoundException Se o arquivo não for encontrado.
     */
    private static void binarySearchTest(int[] array, int size, int target) throws FileNotFoundException {
        long sumComparisons = 0, sumExecutionTime = 0, sumMemoryUsage = 0;
        long startTime = System.nanoTime();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        SearchResult result = binarySearch(array, target);

        printExecutionTime(startTime, beforeUsedMem, result.getComparisons(), STR."binarySearchTest", size, result.getIndex(), target);
    }
    /**
     * Este método realiza uma busca binária em um array.
     * Foi separado somente a busca para facilitar as medidas dos testes.
     *
     * @param array O array no qual a busca será realizada.
     * @param target O alvo da busca.
     * @return Retorna o índice do alvo se ele for encontrado, caso contrário, retorna -1.
     */
    public static SearchResult binarySearch(int[] array, int target) {
        int left = 0;
        int right = array.length - 1;
        int comparisons = 0;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            comparisons++;

            if (array[mid] == target) {
                return new SearchResult(mid, comparisons);
            } else if (array[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return new SearchResult(-1, comparisons);
    }

    /**
     * Este método realiza uma busca sequencial em um array.
     *
     * @param size O tamanho do array.
     * @param target O alvo da busca.
     * @throws FileNotFoundException Se o arquivo não for encontrado.
     */
    private static void sequentialTest(int[] array, int size, int target) throws FileNotFoundException {
        long sumComparisons = 0, sumExecutionTime = 0, sumMemoryUsage = 0;
        long startTime = System.nanoTime();
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        SearchResult result = sequentialSearch(array, target);

        printExecutionTime(startTime, beforeUsedMem, result.getComparisons(), STR."sequentialTest", size, result.getIndex(), target);
    }
    /**
     * Este método realiza uma busca sequencial em um array.
     * Foi separado somente a busca para facilitar as medidas dos testes.
     *
     * @param array O array no qual a busca será realizada.
     * @param target O alvo da busca.
     * @return Retorna o índice do alvo se ele for encontrado, caso contrário, retorna -1.
     */
    public static SearchResult sequentialSearch(int[] array, int target) {
        int comparisons = 0;
        for (int i = 0; i < array.length; i++) {
            comparisons++;
            if (array[i] == target) {
                return new SearchResult(i, comparisons);
            }
        }
        return new SearchResult(-1, comparisons);
    }


    /**
     * Este método preenche uma árvore binária com elementos de um arquivo.
     *
     * @param tree A árvore a ser preenchida.
     * @param fileName O nome do arquivo de onde os elementos serão lidos.
     */
    private static void fillTreeFromFile(BinaryTree tree, String fileName) {
        long startTime = System.currentTimeMillis();

        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextInt()) {
                tree.insert(scanner.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println(STR."File not found: \{fileName}");
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(STR."Execution time fillTreeFromFile \{fileName}: \{endTime - startTime} milliseconds");
    }
    /**
     * Este método preenche uma lista com elementos de um arquivo.
     *
     * @param list A lista a ser preenchida.
     * @param fileName O nome do arquivo de onde os elementos serão lidos.
     */
    private static void fillListFromFile(LinkedList list, String fileName) {
        long startTime = System.currentTimeMillis();

        File file = new File(fileName);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextInt()) {
                list.insertAtStart(scanner.nextInt());
            }
        } catch (FileNotFoundException e) {
            System.out.println(STR."File not found: \{fileName}");
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println(STR."Execution time fillListFromFile \{fileName}: \{endTime - startTime} milliseconds");
    }
    /**
     * Este método preenche um array com elementos de um arquivo.
     *
     * @param array O array a ser preenchido.
     * @param fileName O nome do arquivo de onde os elementos serão lidos.
     */
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

    /**
     * Este método cria uma planilha Excel com os resultados dos testes.
     *
     * @param constant O tamanho do conjunto de dados.
     * @param searchType O tipo de busca (por exemplo, "sequentialTest", "binarySearchTest").
     */
    private static void createSheet(int constant, String searchType) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(STR."Data \{constant} - \{searchType}");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Search Type");
            header.createCell(1).setCellValue("Size");
            header.createCell(2).setCellValue("Execution Time (nanoseconds)");
            header.createCell(3).setCellValue("Memory Usage (bytes)");
            header.createCell(4).setCellValue("Comparisons");
            header.createCell(5).setCellValue("Index");
            header.createCell(6).setCellValue("Target");

            FileOutputStream fileOut = new FileOutputStream(STR."C:/EDAA/data\{constant}_\{searchType}.xlsx");
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
            header.createCell(1).setCellValue("Size");
            header.createCell(2).setCellValue("AVG Execution Time (nanoseconds)");
            header.createCell(3).setCellValue("AVG Memory Usage (bytes)");
            header.createCell(4).setCellValue("AVG Comparisons");

            FileOutputStream fileOut = new FileOutputStream(STR."C:/EDAA/dataAVG.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método cria arquivos com elementos aleatórios.
     */
    public static void createFiles() {
        long startTime = System.nanoTime();

        genarateElements(ONE_HUNDRED_THOUSAND);
        genarateElements(TWO_HUNDRED_THOUSAND);
        genarateElements(THREE_HUNDRED_THOUSAND);
        genarateElements(FOUR_HUNDRED_THOUSAND);
        genarateElements(FIVE_HUNDRED_THOUSAND);
        genarateElements(SIX_HUNDRED_THOUSAND);
        genarateElements(SEVEN_HUNDRED_THOUSAND);
        genarateElements(EIGHT_HUNDRED_THOUSAND);
        genarateElements(NINE_HUNDRED_THOUSAND);
        genarateElements(ONE_MILLION);

        long endTime = System.nanoTime();
        System.out.println(STR."genarateElementsAndFiles execution time: \{endTime - startTime} nanoseconds");
    }

    /**
     * Este método gera elementos aleatórios e os salva em um arquivo.
     *
     * @param size O tamanho do array de elementos a ser gerado.
     */
    public static void genarateElements(int size) {
        int[] arr = generateUniqueRandomArray(size);
        try {
            saveElementsToFile(arr, STR."C:/EDAA/randomElements_\{size}.txt");
        } catch (IOException e) {
            System.out.println("Something went wrong =(");
            e.printStackTrace();
        }
    }

    /**
     * Este método gera um array de inteiros aleatórios e únicos.
     *
     * @param size O tamanho do array a ser gerado.
     * @return Retorna um array de inteiros aleatórios e únicos.
     */
    public static int[] generateUniqueRandomArray(int size) {
        Random random = new Random();
        Set<Integer> set = new LinkedHashSet<>();
        while (set.size() < size) {
            set.add(random.nextInt(size) + 1);
        }
        Integer[] arr = set.toArray(new Integer[0]);
        return Arrays.stream(arr).mapToInt(Integer::intValue).toArray();
    }

    /**
     * Este método salva elementos de um array em um arquivo.
     *
     * @param arr O array de inteiros a ser salvo no arquivo.
     * @param fileName O nome do arquivo onde os elementos serão salvos.
     * @throws IOException Se ocorrer um erro de entrada/saída.
     */
    public static void saveElementsToFile(int[] arr, String fileName) throws IOException {
        PrintWriter writer = new PrintWriter(fileName);
        for (int num : arr) {
            writer.println(num);
        }
        writer.close();
    }

    /**
     * Este método verifica se existem elementos duplicados em um arquivo.
     *
     * @param fileName O nome do arquivo a ser verificado.
     * @return Retorna verdadeiro se houver elementos duplicados no arquivo, falso caso contrário.
     * @throws FileNotFoundException Se o arquivo não for encontrado.
     */
    public static boolean hasDuplicatesInFile(String fileName) throws FileNotFoundException {
        Set<Integer> set = new HashSet<>();
        List<Integer> list = new ArrayList<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextInt()) {
            int num = scanner.nextInt();
            set.add(num);
            list.add(num);
        }

        return set.size() != list.size();
    }

    /**
     * Este método imprime o tempo de execução de uma operação de busca.
     *
     * @param startTime O tempo de início da operação em nanossegundos.
     * @param searchType O tipo da operação de busca (por exemplo, "binarySearch", "sequentialSearch").
     * @param size O tamanho do conjunto de dados no qual a operação foi realizada.
     * @param index O índice no qual o alvo foi encontrado, ou -1 se o alvo não foi encontrado.
     */
    private static void printExecutionTime(long startTime, long beforeUsedMem, int comparisons,
                                           String searchType, int size, int index, int target) {
        long endTime = System.nanoTime();

        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoryUsage = afterUsedMem - beforeUsedMem;

        System.out.println(STR." ");
        System.out.println(STR."\{searchType} \{size} - target: \{target}");
        System.out.println(STR."\{searchType} \{size} - execution time: \{endTime - startTime} nanoseconds");
        System.out.println(STR."\{searchType} \{size} - memory usage: \{memoryUsage} bytes");
        System.out.println(STR."\{searchType} \{size} - comparisons: \{comparisons} x");
        if (index != -1) {
            System.out.println(STR."Found at: \{index}");
        } else {
            System.out.println("Not Found.");
        }

        System.out.println(STR." ");
        System.out.println(STR."---------------------------------------------------------------------------------");

        try {
            FileInputStream fileIn = new FileInputStream(STR."C:/EDAA/data\{size}_\{searchType}.xlsx");
            Workbook workbook = new XSSFWorkbook(fileIn);
            Sheet sheet = workbook.getSheetAt(0);

            int lastRowNum = sheet.getLastRowNum();
            Row dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(searchType);
            dataRow.createCell(1).setCellValue(size);
            dataRow.createCell(2).setCellValue(endTime - startTime);
            dataRow.createCell(3).setCellValue(memoryUsage);
            dataRow.createCell(4).setCellValue(comparisons);
            dataRow.createCell(5).setCellValue(index);
            dataRow.createCell(6).setCellValue(target);

            fileIn.close();

            FileOutputStream fileOut = new FileOutputStream(STR."C:/EDAA/data\{size}_\{searchType}.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}