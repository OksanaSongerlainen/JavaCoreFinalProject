package service;

import java.io.*;
import java.time.LocalDateTime;

public class ReportService {
    private static final String REPORT_FILE = "report.txt";

    public void addReportEntry(String fileName, String operation, String status) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE, true))) {
            String entry = LocalDateTime.now() + " | " + fileName + " | " + operation + " | " + status;
            writer.println(entry);
        } catch (IOException e) {
            System.out.println("Ошибка при записи в отчет: " + e.getMessage());
        }
    }

    public void printReport() {
        File file = new File(REPORT_FILE);
        if (!file.exists()) {
            System.out.println("Файл отчета еще не создан.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n=== ОТЧЕТ О ПЕРЕВОДАХ ===");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("=== КОНЕЦ ОТЧЕТА ===\n");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении отчета: " + e.getMessage());
        }
    }
}
