package loadTesting;

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

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(ConcurrentTestRunner.class)
public class MultiThreadTransfersTest {

    public static AccountService accountService = new AccountServiceImpl();
    public static TransferService transferService = new TransferServiceImpl(accountService);
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadTransfersTest.class);
    private final static int THREAD_COUNT2 = 10;

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
    }

    @ThreadCount(THREAD_COUNT2)
    @Test
    public void testPostAccountLoad() throws SQLException, NoSuchAccountException {
        Transfer transfer = new Transfer();
        transfer.setAccountFromId(new BigInteger("4000123412341234"));
        transfer.setAccountToId(new BigInteger("4000123412341235"));
        transfer.setSumToTransfer(5.0);
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));

        transferService.createTransfer(transfer);

        Account accountFrom = accountService.getAccount(new BigInteger("4000123412341234"));
        System.out.println("accountFrom has " + accountFrom.getBalance());
        Account accountTo = accountService.getAccount(new BigInteger("4000123412341235"));
        System.out.println("accountTo has " + accountTo.getBalance());
    }

    @After
    public void testAccountsCount() throws SQLException, NoSuchAccountException {
        Account accountFrom = accountService.getAccount(new BigInteger("4000123412341234"));
        Double expectedBalanceFrom = 1000.0 - 5.0 * THREAD_COUNT2;
        LOGGER.info("AccountFrom has " + accountFrom.getBalance() + " on balance, should be " + expectedBalanceFrom);
        assertEquals(expectedBalanceFrom, accountFrom.getBalance());

        Account accountTo = accountService.getAccount(new BigInteger("4000123412341235"));
        Double expectedBalanceTo = CurrencyConverter.currencyConverter(5.0,
                "RUB", "USD") * THREAD_COUNT2;
        LOGGER.info("AccountTo has " + accountTo.getBalance() + " on balance, should be " + expectedBalanceTo);
        assertEquals(expectedBalanceTo, accountTo.getBalance());
    }
}
