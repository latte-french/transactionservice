package service;

import model.Account;
import model.exceptions.NoAccountsExistException;
import model.exceptions.NoSuchAccountException;
import model.exceptions.NoUserAccountsException;

import java.math.BigInteger;
import java.util.List;

public interface AccountService {

    Account getAccount(BigInteger id) throws NoSuchAccountException;

    List<Account> getAccounts() throws NoAccountsExistException;

    List<Account> getAccountsOfUser(BigInteger userId) throws NoUserAccountsException;

    void createAccount(Account account, BigInteger userId);

    void removeAccount(BigInteger id) throws NoSuchAccountException;

    void updateAccount(Account account) throws NoSuchAccountException;
}
