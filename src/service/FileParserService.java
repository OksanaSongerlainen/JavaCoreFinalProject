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
    public Transaction parseTransactionFile(File file) throws InvalidAccountFormatException,
            InvalidAmountException {
        String fromAccount = null;
        String toAccount = null;
        Integer amount = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                Pattern accountPattern = Pattern.compile("\\d{5}-\\d{5}");
                Matcher accountMatcher = accountPattern.matcher(line);

                while (accountMatcher.find()) {
                    String foundAccount = accountMatcher.group();
                    if (fromAccount == null) {
                        fromAccount = foundAccount;
                    } else if (toAccount == null && !foundAccount.equals(fromAccount)) {
                        toAccount = foundAccount;
                    }
                }


                Pattern amountPattern = Pattern.compile("-?\\d+");
                Matcher amountMatcher = amountPattern.matcher(line);
                if (amountMatcher.find() && amount == null) {
                    amount = Integer.parseInt(amountMatcher.group());
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

        return new Transaction(fromAccount, toAccount, amount);
    }
}
