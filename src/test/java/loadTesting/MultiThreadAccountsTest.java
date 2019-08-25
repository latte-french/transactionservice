package loadTesting;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
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
import utils.DatabaseCleanup;

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
    private long startTime;
    private long finishTime;

    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        startTime = System.nanoTime();
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
        finishTime = System.nanoTime();

        List<Account> databaseAccounts = accountService.getAccounts();

        //Should be (THREAD_COUNT = 3) accounts in database because 3 accounts were loaded in @Before
        LOGGER.info("There are " + databaseAccounts.size() + " accounts in database, should be " + (THREAD_COUNT + 3));
        assertEquals(THREAD_COUNT + 3, databaseAccounts.size());

        long timeElapsedInMillis = (finishTime - startTime)/1000000;

        LOGGER.info("Execution time in milliseconds: " + timeElapsedInMillis);
       // assertTrue(timeElapsedInMillis < 200);
    }
}