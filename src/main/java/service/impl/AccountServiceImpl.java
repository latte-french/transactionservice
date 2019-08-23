package service.impl;

import dataStore.AccountStore;
import model.Account;
import model.exceptions.NoAccountsExistException;
import model.exceptions.NoSuchAccountException;
import model.exceptions.NoUserAccountsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

public class AccountServiceImpl implements AccountService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    public Account getAccount(BigInteger id) throws NoSuchAccountException, SQLException {
        Account account = AccountStore.getAccountFromDB(id);
        if (account.getId() == null)  {
            LOGGER.error("Account with id " + id + " doesn't exist");
            throw new NoSuchAccountException(id);
        }
        return account;
    }

    public void createAccount(Account account, BigInteger userId) throws SQLException{
        AccountStore.putAccountToDB(account,userId);
    }

    public List<Account> getAccounts() throws NoAccountsExistException, SQLException{
        List<Account> accounts = AccountStore.getAccountsFromDB();
        if (accounts.size() == 0)  {
            LOGGER.error("No accounts exist in the database");
            throw new NoAccountsExistException();
        }
        return accounts;
    }

    public List<Account> getAccountsOfUser (BigInteger id) throws NoUserAccountsException, SQLException{
        List<Account> userAccounts = AccountStore.getAccountsOfUserFromDB(id);
        if (userAccounts.size() == 0)  {
            LOGGER.error("No accounts belong to the user with id = " + id);
            throw new NoUserAccountsException(id);
        }
        return userAccounts;
    }

    public void removeAccount(BigInteger id) throws NoSuchAccountException, SQLException {
        if (getAccount(id) != null) {
            AccountStore.removeAccountFromDB(id);
        }
    }

    public void updateAccount(Account account) throws NoSuchAccountException, SQLException{
        if (getAccount(account.getId()) != null) {
            AccountStore.updateAccountInDB(account);
        }
    }

}
