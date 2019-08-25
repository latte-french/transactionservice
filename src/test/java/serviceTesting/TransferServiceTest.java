package serviceTesting;

import dataStore.DatabaseCreation;
import model.Account;
import model.Transfer;
import model.exceptions.BalanceNotEnoughException;
import model.exceptions.NoAccountsExistException;
import model.exceptions.NoSuchAccountException;
import org.junit.Before;
import org.junit.Test;
import service.AccountService;
import service.TransferService;
import service.impl.AccountServiceImpl;
import service.impl.TransferServiceImpl;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TransferServiceTest {
    
    public static AccountService accountService = new AccountServiceImpl();
    public static TransferService transferService = new TransferServiceImpl(accountService);
    
    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Test
    /*positive test*/
    public void testPostTransferImpl() throws SQLException, NoSuchAccountException {
        Transfer transfer = new Transfer();
        transfer.setAccountFromId(new BigInteger("4000123412341234"));
        transfer.setAccountToId(new BigInteger("4000123412341235"));
        transfer.setSumToTransfer(5.0);
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));

        transferService.createTransfer(transfer);

        Transfer expectedTransfer = new Transfer(new BigInteger("4000123412341234"),
                new BigInteger("4000123412341235"), 5.0, 0.0755,
                new Timestamp(new Date().getTime()));

        Transfer actualTransfer = transferService.getTransfers().get(2);

        assertEquals(expectedTransfer.getAccountFromId(), actualTransfer.getAccountFromId());
        assertEquals(expectedTransfer.getAccountToId(), actualTransfer.getAccountToId());
        assertEquals(expectedTransfer.getSumToTransfer(), actualTransfer.getSumToTransfer());
        assertEquals(expectedTransfer.getSumTransferred(), actualTransfer.getSumTransferred());

        Account accountFrom = accountService.getAccount(new BigInteger("4000123412341234"));
        assertEquals(ModelsInitialization.accountTransferredFrom, accountFrom);

        Account accountTo = accountService.getAccount(new BigInteger("4000123412341235"));
        assertEquals(ModelsInitialization.accountTransferredTo, accountTo);
    }


    @Test
    /*positive test*/
    public void testGetTransfersImpl() throws SQLException {
        assertEquals(ModelsInitialization.transfersForTest, transferService.getTransfers());
    }


    @Test(expected = NoSuchAccountException.class)
    /*negative test when accountFrom doesn't exist*/
    public void testPostTransferAccountFromNonExistImpl() throws SQLException, NoSuchAccountException {
        Transfer transfer = new Transfer();
        transfer.setAccountFromId(new BigInteger("1"));
        transfer.setAccountToId(new BigInteger("4000123412341234"));
        transfer.setSumToTransfer(5.0);
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));

        transferService.createTransfer(transfer);

        assertEquals(transferService.getTransfers().size(), 0);

        Account accountTo = accountService.getAccount(new BigInteger("4000123412341234"));
        assertEquals(ModelsInitialization.accountForTest, accountTo);
    }


    @Test(expected = NoSuchAccountException.class)
    /*negative test when accountTo doesn't exist*/
    public void testPostTransferAccountToNonExistImpl() throws SQLException, NoSuchAccountException {
        Transfer transfer = new Transfer();
        transfer.setAccountFromId(new BigInteger("4000123412341234"));
        transfer.setAccountToId(new BigInteger("1"));
        transfer.setSumToTransfer(5.0);
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));

        transferService.createTransfer(transfer);

        assertEquals(transferService.getTransfers().size(), 2);

        Account accountFrom = accountService.getAccount(new BigInteger("4000123412341234"));
        assertEquals(ModelsInitialization.accountForTest, accountFrom);

    }


    @Test(expected = NoAccountsExistException.class)
    /*negative test when no accounts in database*/
    public void testPostTransferNoAccountsExistImpl() throws SQLException, NoSuchAccountException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        Transfer transfer = new Transfer();
        transfer.setAccountFromId(new BigInteger("4000123412341234"));
        transfer.setAccountToId(new BigInteger("4000123412341235"));
        transfer.setSumToTransfer(5.0);
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));

        transferService.createTransfer(transfer);

        assertEquals(transferService.getTransfers().size(), 2);
    }


    @Test(expected = BalanceNotEnoughException.class)
    /*negative test when balance is not enough*/
    public void testPostTransferBalanceNotEnoughImpl() throws SQLException, NoSuchAccountException {
        Transfer transfer = new Transfer();
        transfer.setAccountFromId(new BigInteger("4000123412341234"));
        transfer.setAccountToId(new BigInteger("4000123412341235"));
        transfer.setSumToTransfer(10000.0);
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));

        transferService.createTransfer(transfer);

        assertEquals(transferService.getTransfers().size(), 2);

        Account accountFrom = accountService.getAccount(new BigInteger("4000123412341234"));
        assertEquals(ModelsInitialization.accountsForTest.get(0), accountFrom);

        Account accountTo = accountService.getAccount(new BigInteger("4000123412341235"));
        assertEquals(ModelsInitialization.accountsForTest.get(1), accountTo);
    }
}
