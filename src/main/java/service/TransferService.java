package service;

import model.Transfer;
import model.exceptions.BalanceNotEnoughException;
import model.exceptions.NoSuchAccountException;
import model.exceptions.NoTransfersExistException;

import java.util.List;

public interface TransferService {

    void createTransfer(Transfer transfer) throws NoSuchAccountException, BalanceNotEnoughException;

    List<Transfer> getTransfers() throws NoTransfersExistException;
}
