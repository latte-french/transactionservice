package nonFunctionalTesting;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import model.Account;
import model.Transfer;
import model.exceptions.NoSuchAccountException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.TransferService;
import service.impl.AccountServiceImpl;
import service.impl.TransferServiceImpl;
import utils.CurrencyConverter;
import utils.DatabaseCleanup;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(ConcurrentTestRunner.class)
public class MultiThreadTransfersTest {

    public static AccountService accountService = new AccountServiceImpl();
    public static TransferService transferService = new TransferServiceImpl(accountService);
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadTransfersTest.class);
    private final static int THREAD_COUNT2 = 50;
    private long startTime;
    private long finishTime;

    @Before
    public void resetDatabase() throws SQLException, NoSuchAccountException {
        DatabaseCleanup.prepareDatabase();

        Account accountFrom = accountService.getAccount(new BigInteger("4000123412341234"));
        Account accountFromChanges = new Account();
        accountFromChanges.setBalance(1000.0);

        Account accountTo = accountService.getAccount(new BigInteger("4000123412341235"));
        Account accountToChanges = new Account();
        accountToChanges.setBalance(0.0);

        accountService.updateAccount(accountFrom, accountFromChanges);
        accountService.updateAccount(accountTo, accountToChanges);

        startTime = System.nanoTime();
    }

    @ThreadCount(THREAD_COUNT2)
    @Test
    public void testPostTransfersLoad() throws SQLException, NoSuchAccountException {
        Transfer transfer = new Transfer();
        transfer.setAccountFromId(new BigInteger("4000123412341234"));
        transfer.setAccountToId(new BigInteger("4000123412341235"));
        transfer.setSumToTransfer(5.0);
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));

        transferService.createTransfer(transfer);

    }

    @After
    public void testAccountsBalanceAfterTransfers() throws SQLException, NoSuchAccountException {
        finishTime = System.nanoTime();

        Account accountFrom = accountService.getAccount(new BigInteger("4000123412341234"));
        Double expectedBalanceFrom = 1000.0 - 5.0 * THREAD_COUNT2;

        BigDecimal bdFrom = BigDecimal.valueOf(accountFrom.getBalance());
        bdFrom = bdFrom.setScale(3, RoundingMode.HALF_UP);
        Double actualBalanceFrom = bdFrom.doubleValue();

        LOGGER.info("AccountFrom has " + actualBalanceFrom + " on balance, should be " + expectedBalanceFrom);
        assertEquals(expectedBalanceFrom, actualBalanceFrom);

        Account accountTo = accountService.getAccount(new BigInteger("4000123412341235"));
        Double expectedBalanceTo = CurrencyConverter.currencyConverter(5.0,
                "RUB", "USD") * THREAD_COUNT2;

        BigDecimal bdTo = BigDecimal.valueOf(accountTo.getBalance());
        bdTo = bdTo.setScale(3, RoundingMode.HALF_UP);
        Double actualBalanceTo = bdTo.doubleValue();

        LOGGER.info("AccountTo has " + actualBalanceTo + " on balance, should be " + expectedBalanceTo);
        assertEquals(expectedBalanceTo, actualBalanceTo);

        long timeElapsedInMillis = (finishTime - startTime)/1000000;

        LOGGER.info("Execution time in milliseconds: " + timeElapsedInMillis);
       // assertTrue(timeElapsedInMillis < 500);

    }
}
