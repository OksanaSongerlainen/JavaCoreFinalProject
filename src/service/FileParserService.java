package service;

import exception.InvalidAccountFormatException;
import exception.InvalidAmountException;
import model.Transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParserService {
    public Transaction parseTransactionFile(File file) {
        String fromAccount = null;
        String toAccount = null;
        Integer amount = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.toLowerCase().contains("счет") || line.toLowerCase().contains("account")) {
                    Pattern accountPattern = Pattern.compile("\\d{5}-\\d{5}");
                    Matcher accountMatcher = accountPattern.matcher(line);
                }
                if (line.toLowerCase().contains("сумма") || line.toLowerCase().contains("amount")) {
                    Pattern amountPattern = Pattern.compile("\\d+");
                    Matcher amountMatcher = amountPattern.matcher(line);
                }
            }

        } catch (IOException e) {
            throw new InvalidAmountException("Ошибка чтения файла: " + e.getMessage());
        }


        if (fromAccount == null || toAccount == null) {
            throw new InvalidAccountFormatException("Не найдены номера счетов в файле");
        }

        if (amount == null) {
            throw new InvalidAmountException("Не найдена сумма перевода в файле");
        }

        if (amount <= 0) {
            throw new InvalidAmountException("Сумма перевода должна быть положительной: " + amount);
        }

        return new Transaction(fromAccount, toAccount, amount, file.getName());
    }
}