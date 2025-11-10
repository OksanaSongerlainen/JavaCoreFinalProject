public class Main {
    private static AccountService accountService = new AccountService();
    private static FileParserService fileParserService = new FileParserService();
    private static ReportService reportService = new ReportService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== СИСТЕМА ДЕНЕЖНЫХ ПЕРЕВОДОВ ===");

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Обработать файлы переводов из папки input");
            System.out.println("2 - Показать отчет о переводах");
            System.out.println("3 - Выход");
            System.out.print("Ваш выбор: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    processFiles();
                    break;
                case "2":
                    reportService.printReport();
                    break;
                case "3":
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static void processFiles() {
        File inputDir = new File("input");
        if (!inputDir.exists() || !inputDir.isDirectory()) {
            System.out.println("Папка input не существует!");
            return;
        }

        File[] files = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("В папке input нет txt файлов для обработки.");
            return;
        }

        System.out.println("Найдено файлов для обработки: " + files.length);

        for (File file : files) {
            processSingleFile(file);
        }
    }

    private static void processSingleFile(File file) {
        try {
            // Парсим файл
            Transaction transaction = fileParserService.parseTransactionFile(file);

            // Проверяем форматы счетов
            accountService.validateAccountNumber(transaction.getFromAccount());
            accountService.validateAccountNumber(transaction.getToAccount());

            // Выполняем перевод
            accountService.transferMoney(
                    transaction.getFromAccount(),
                    transaction.getToAccount(),
                    transaction.getAmount()
            );

            // Записываем успех в отчет
            String operation = transaction.toString();
            reportService.addReportEntry(file.getName(), operation, "успешно обработан");
            System.out.println("✓ Файл " + file.getName() + " обработан успешно");

        } catch (Exception e) {
            // Записываем ошибку в отчет
            String operation = "ошибка во время обработки";
            reportService.addReportEntry(file.getName(), operation, e.getMessage());
            System.out.println("✗ Ошибка в файле " + file.getName() + ": " + e.getMessage());
        }

        // Перемещаем файл в archive
        moveFileToArchive(file);
    }

    private static void moveFileToArchive(File file) {
        File archiveDir = new File("archive");
        if (!archiveDir.exists()) {
            archiveDir.mkdir();
        }

        File newLocation = new File(archiveDir, file.getName());
        if (file.renameTo(newLocation)) {
            System.out.println("Файл перемещен в archive: " + file.getName());
        } else {
            System.out.println("Не удалось переместить файл в archive: " + file.getName());
        }
    }
}
