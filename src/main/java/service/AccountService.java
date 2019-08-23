package service;

import model.Account;
import model.exceptions.NoAccountsExistException;
import model.exceptions.NoSuchAccountException;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

public interface AccountService {

    Account getAccount(BigInteger id) throws NoSuchAccountException, SQLException;

    List<Account> getAccounts() throws NoAccountsExistException, SQLException;

    void createAccount(Account account, BigInteger userId) throws SQLException;

    void removeAccount(BigInteger id) throws NoSuchAccountException, SQLException;

    void updateAccount(Account account, Account accountChanges) throws NoSuchAccountException, SQLException;
}
