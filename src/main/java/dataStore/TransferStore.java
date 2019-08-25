package dataStore;

import dataStore.statementsCreation.AccountStatementCreation;
import dataStore.statementsCreation.TransferStatementCreation;
import dataStore.utils.EntityConverters;
import dataStore.utils.StatementExecution;
import model.Account;
import model.StatementModel;
import model.Transfer;
import model.exceptions.NoSuchAccountException;
import service.AccountService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransferStore {

    private static ResultSet result;
    private static AccountService accountService;
    private static StatementModel statementModel;
    private static ArrayList<StatementModel> statementModels;

    public TransferStore (AccountService accountService){
        this.accountService = accountService;
    }


    public static void putTransferToDB(Transfer transfer, Account accountFrom, Account accountTo) throws NoSuchAccountException, SQLException {
        statementModels = new ArrayList<>();

        statementModels.add(TransferStatementCreation.putTransferStatement(transfer, accountFrom, accountTo));
        statementModels.add(AccountStatementCreation.updateAccountStatement(accountFrom));
        statementModels.add(AccountStatementCreation.updateAccountStatement(accountTo));

        StatementExecution.prepareAndExecuteStatements(statementModels);
    }

    public static List<Transfer> getTransfersFromDB() throws SQLException{
        List<Transfer> transferCollection = new ArrayList<Transfer>();
        statementModel = TransferStatementCreation.getTransfersStatement();

        result = StatementExecution.prepareAndExecuteQuery(statementModel);
        while (result.next()){
            transferCollection.add(EntityConverters.convertFromEntityToTransfer(result));
        }
        return transferCollection;
    }
}
