package service.impl;

import dataStore.TransferStore;
import model.Account;
import model.Transfer;
import model.exceptions.BalanceNotEnoughException;
import model.exceptions.NoSuchAccountException;
import model.exceptions.NoTransfersExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.TransferService;
import utils.CurrencyConverter;

import java.util.List;

public class TransferServiceImpl implements TransferService {

    private static TransferServiceImpl instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);
    final CurrencyConverter currencyConverter = CurrencyConverter.getInstance();
    final TransferStore transferStore = TransferStore.getInstance();
    final AccountService accountService = AccountServiceImpl.getInstance();

    public static TransferService getInstance() {
        if (instance == null) {
            instance = new TransferServiceImpl();
        }
        return instance;
    }

    public void createTransfer(Transfer transfer) throws NoSuchAccountException, BalanceNotEnoughException{
            Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
            Account accountTo = accountService.getAccount(transfer.getAccountToId());
            Double sumToTransfer = transfer.getSumToTransfer();

            if ((accountFrom.getBalance() - sumToTransfer) <0) {
                LOGGER.error("Account with id " + accountFrom.getId() + " can't transfer " + sumToTransfer + " " +
                        accountFrom.getCurrency() + ", the balance is only " + accountFrom.getBalance() + " " +
                        accountFrom.getCurrency());
                throw new BalanceNotEnoughException(accountFrom, sumToTransfer);
            }
            transfer.setSumTransferred(currencyConverter.currencyConverter(sumToTransfer, accountFrom.getCurrency(),
                    accountTo.getCurrency()));

            accountFrom.setBalance(accountFrom.getBalance() - sumToTransfer);
            accountTo.setBalance(accountTo.getBalance() + transfer.getSumTransferred());

            transferStore.putTransferToDB(transfer);
            accountService.updateAccount(accountFrom);
            accountService.updateAccount(accountTo);
    }

    public List<Transfer> getTransfers() throws NoTransfersExistException {
        List<Transfer> transfers = transferStore.getTransfersFromDB();
        if (transfers.size() == 0){
            LOGGER.error("No transfers exist in the database");
            throw new NoTransfersExistException();
        }
        return transferStore.getTransfersFromDB();
    }
}
