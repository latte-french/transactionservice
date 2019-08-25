package loadTesting;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import model.Account;
import model.exceptions.NoSuchAccountException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import service.AccountService;
import service.impl.AccountServiceImpl;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(ConcurrentTestRunner.class)
public class MultiThreadAccountsTest {

    public static AccountService accountService = new AccountServiceImpl();
    private final static int THREAD_COUNT = 5;

    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Ignore
    @ThreadCount(THREAD_COUNT)
    @Test
    public void testPostAccountLoad() throws SQLException, NoSuchAccountException {
        BigInteger accountId = new BigInteger(32, new Random());
        Account account = new Account(accountId, 5.0, "RUB");
        accountService.createAccount(account, new BigInteger("2"));
    }

    @After
    public void testAccountsCount() throws SQLException {
        assertEquals(8, accountService.getAccounts().size());
    }
}