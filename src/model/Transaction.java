package model;

public class Transaction {
    private String fromAccount;
    private String toAccount;
    private int amount;
    private String fileName;

    public Transaction(String fromAccount, String toAccount, int amount, String fileName) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.fileName = fileName;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public int getAmount() {
        return amount;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount=" + amount +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
