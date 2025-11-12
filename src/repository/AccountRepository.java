package repository;

import model.Account;
import java.util.List;

public interface AccountRepository {
    List<Account> findAll();
    void saveAll(List<Account> accounts);
    Account findByNumber(String accountNumber);
}