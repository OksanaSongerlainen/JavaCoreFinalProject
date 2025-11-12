package service;

import exception.AccountNotFoundException;
import exception.InvalidAccountFormatException;
import model.Account;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private Map<String, Account> accounts = new HashMap<>();

    public AccountService() {
        loadAccounts();
    }

    private void loadAccounts() {
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) {

            accounts.put("12345-67890", new Account("12345-67890", 1000));
            accounts.put("23456-78901", new Account("23456-78901", 2000));
            accounts.put("34567-89012", new Account("34567-89012", 1500)); // ДОБАВЬТЕ ЕЩЕ СЧЕТА
            saveAccounts();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String accountNumber = parts[0].trim();
                    int balance = Integer.parseInt(parts[1].trim());
                    accounts.put(accountNumber, new Account(accountNumber, balance));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке счетов: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка формата данных в файле счетов: " + e.getMessage());
        }
    }

    public void saveAccounts() {
        // Создаем папку если нужно
        File file = new File(ACCOUNTS_FILE);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Account account : accounts.values()) {
                writer.println(account.getAccountNumber() + "|" + account.getBalance());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении счетов: " + e.getMessage());
        }
    }

    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Счет не найден: " + accountNumber);
        }
        return account;
    }

    public void validateAccountNumber(String accountNumber) throws InvalidAccountFormatException {
        if (!accountNumber.matches("\\d{5}-\\d{5}")) {
            throw new InvalidAccountFormatException("Неверный формат счета: " + accountNumber);
        }
    }

    public void transferMoney(String fromAccount, String toAccount, int amount) {
        Account from = getAccount(fromAccount);
        Account to = getAccount(toAccount);

        if (from.getBalance() < amount) {
            throw new RuntimeException("Недостаточно средств на счете " + fromAccount);
        }

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
        saveAccounts();
    }
}