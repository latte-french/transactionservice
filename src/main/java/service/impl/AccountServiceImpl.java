package service.impl;

import dataStore.AccountStore;
import model.Account;
import model.exceptions.NoAccountsExistException;
import model.exceptions.NoSuchAccountException;
import model.exceptions.NoUserAccountsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;

import javax.inject.Singleton;
import java.math.BigInteger;
import java.util.List;

@Singleton
public class AccountServiceImpl implements AccountService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private static AccountServiceImpl instance;

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountServiceImpl();
        }
        return instance;
    }


    public Account getAccount(BigInteger id) throws NoSuchAccountException {
        Account account = AccountStore.getAccountFromDB(id);
        if (account.getId() == null)  {
            LOGGER.error("Account with id " + id + " doesn't exist");
            throw new NoSuchAccountException(id);
        }
        return account;
    }

    public void createAccount(Account account, BigInteger userId){
        AccountStore.putAccountToDB(account);
        AccountStore.putUserAccountDependencyToDB(account.getId(), userId);
    }

    public List<Account> getAccounts() throws NoAccountsExistException{
        List<Account> accounts = AccountStore.getAccountsFromDB();
        if (accounts.size() == 0)  {
            LOGGER.error("No accounts exist in the database");
            throw new NoAccountsExistException();
        }
        return accounts;
    }

    public List<Account> getAccountsOfUser (BigInteger id) throws NoUserAccountsException{
        List<Account> userAccounts = AccountStore.getAccountsOfUserFromDB(id);
        if (userAccounts.size() == 0)  {
            LOGGER.error("No accounts belong to the user with id = " + id);
            throw new NoUserAccountsException(id);
        }
        return userAccounts;
    }

    public void removeAccount(BigInteger id) throws NoSuchAccountException {
        if (getAccount(id) != null) {
            AccountStore.removeAccountFromDB(id);
            AccountStore.removeUserAccountDependencyFromDB(id);
        }
    }

    public void updateAccount(Account account) throws NoSuchAccountException{
        if (getAccount(account.getId()) != null) {
            AccountStore.updateAccountInDB(account);
        }
    }

}
