package loadTesting;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import dataStore.DatabaseCreation;
import model.Account;
import model.exceptions.NoSuchAccountException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.impl.AccountServiceImpl;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(ConcurrentTestRunner.class)
public class MultiThreadAccountsTest {

    public static AccountService accountService = new AccountServiceImpl();
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadAccountsTest.class);
    private final static int THREAD_COUNT = 50;

    @Before
    public void resetDatabase() {
        DatabaseCreation.initDatabase();
        //DatabaseCleanup.prepareDatabase();
    }

    @ThreadCount(THREAD_COUNT)
    @Test
    public void testPostAccountLoad() throws SQLException, NoSuchAccountException {
        BigInteger accountId = new BigInteger(32, new Random());
        Account account = new Account(accountId, 5.0, "RUB");
        accountService.createAccount(account, new BigInteger("2"));
    }

    @After
    public void testAccountsCount() throws SQLException {
        List<Account> databaseAccounts = accountService.getAccounts();
        LOGGER.info("There are " + databaseAccounts.size() + " accounts in database, should be " + THREAD_COUNT);
        assertEquals(THREAD_COUNT, databaseAccounts.size());
    }
}