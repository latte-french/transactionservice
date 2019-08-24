package serviceTesting;

import dataStore.DatabaseCreation;
import model.Account;
import model.exceptions.NoAccountsExistException;
import model.exceptions.NoSuchAccountException;
import org.junit.Before;
import org.junit.Test;
import service.AccountService;
import service.impl.AccountServiceImpl;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AccountServiceTest {

    public static AccountService accountService = new AccountServiceImpl();

    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Test
    /*positive test*/
    public void GetAccount()  throws NoSuchAccountException, SQLException {
        assertEquals(ModelsInitialization.accountForTest,
                accountService.getAccount(new BigInteger("4000123412341234")));
    }

    @Test
    /*positive test*/
    public void GetAccounts() throws SQLException {
        assertEquals(ModelsInitialization.accountsForTest, accountService.getAccounts());
    }

    @Test
    /*positive test*/
    public void PostAccount() throws SQLException, NoSuchAccountException {
        Account account = new Account(new BigInteger("4000123412341237"),5.0,"RUB");

        accountService.createAccount(account,new BigInteger("2"));

        assertEquals(account, accountService.getAccount(new BigInteger("4000123412341237")));
    }

    @Test
    /*positive test*/
    public void PutAccountChangeBalance() throws SQLException, NoSuchAccountException {
        Account account = ModelsInitialization.accountForTest;
        Account accountChanges = new Account();
        accountChanges.setBalance(5.0);

        accountService.updateAccount(account, accountChanges);

        account.setBalance(5.0);
        assertEquals(account, accountService.getAccount(new BigInteger("4000123412341234")));
    }

    @Test
    /*positive test*/
    public void PutAccountChangeCurrency() throws SQLException, NoSuchAccountException  {
        Account account = ModelsInitialization.accountForTest;
        Account accountChanges = new Account();
        accountChanges.setCurrency("EUR");

        accountService.updateAccount(account, accountChanges);

        account.setCurrency("EUR");
        assertEquals(account, accountService.getAccount(new BigInteger("4000123412341234")));
    }

    @Test
    /*positive test*/
    public void PutAccountChangeBalanceAndCurrency() throws SQLException, NoSuchAccountException  {
        Account account = ModelsInitialization.accountForTest;
        Account accountChanges = new Account();
        accountChanges.setBalance(5.0);
        accountChanges.setCurrency("EUR");

        accountService.updateAccount(account, accountChanges);

        account.setBalance(5.0);
        account.setCurrency("EUR");
        assertEquals(account, accountService.getAccount(new BigInteger("4000123412341234")));
    }

    @Test
    /*positive test*/
    public void DeleteAccount() throws SQLException, NoSuchAccountException {
        ArrayList<Account> accounts = ModelsInitialization.accountsForTest;
        accounts.remove(0);

        accountService.removeAccount(new BigInteger("4000123412341234"));

        assertEquals(accounts, accountService.getAccounts());
    }

    @Test(expected = NoSuchAccountException.class)
    /*negative test on non-existing account*/
    public void GetNonExistingAccount() throws SQLException, NoSuchAccountException {
        accountService.getAccount(new BigInteger("1"));
    }

    @Test(expected = NoAccountsExistException.class)
    /*negative test on empty database*/
    public void GetAccountEmptyDatabase() throws SQLException, NoAccountsExistException, NoSuchAccountException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        accountService.getAccount(new BigInteger("1"));
    }

    @Test(expected = NoAccountsExistException.class)
    /*negative test on empty accounts table*/
    public void GetAccountsEmpty() throws SQLException, NoAccountsExistException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        accountService.getAccounts();
    }

    @Test(expected = NoSuchAccountException.class)
    /*negative test on non-existing account*/
    public void PutNonExistingAccount() throws SQLException,NoSuchAccountException {
        Account account = ModelsInitialization.accountForTest;
        account.setId(new BigInteger("1"));
        Account accountChanges = new Account();
        accountChanges.setCurrency("EUR");

        accountService.updateAccount(account, accountChanges);;
    }

    @Test(expected = NoAccountsExistException.class)
    /*negative test on empty database*/
    public void PutAccountEmptyDatabase() throws SQLException,NoSuchAccountException, NoAccountsExistException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        Account account = ModelsInitialization.accountForTest;
        Account accountChanges = new Account();
        accountChanges.setCurrency("EUR");

        accountService.updateAccount(account, accountChanges);;
    }

    @Test(expected = NoSuchAccountException.class)
    /*negative test on non-existing account*/
    public void DeleteNonExistingAccount() throws SQLException,NoSuchAccountException {
        accountService.removeAccount(new BigInteger("1"));
    }

    @Test(expected = NoAccountsExistException.class)
    /*negative test on empty database*/
    public void DeleteAccountEmpty() throws SQLException,NoSuchAccountException, NoAccountsExistException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        accountService.removeAccount(new BigInteger("4000123412341234"));
    }
}
