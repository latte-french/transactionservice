package dataStore.statementsCreation;

import model.Account;
import model.StatementModel;
import model.Transfer;

import java.util.ArrayList;

public class TransferStatementCreation {
    private static String statementMessage;
    private static ArrayList<String> statementObjects;

    public static StatementModel putTransferStatement(Transfer transfer, Account accountFrom, Account accountTo)  {

        statementMessage = "INSERT INTO transfers VALUES (null,?,?,?,?,?,?,?)";
        statementObjects = new ArrayList<>();
        statementObjects.add(accountFrom.getId().toString());
        statementObjects.add(transfer.getSumToTransfer().toString());
        statementObjects.add(accountFrom.getCurrency());
        statementObjects.add(accountTo.getId().toString());
        statementObjects.add(transfer.getSumTransferred().toString());
        statementObjects.add(accountTo.getCurrency());
        statementObjects.add(transfer.getTransferredAt().toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel getTransfersStatement() {
        statementMessage = "SELECT * FROM transfers";
        return new StatementModel(statementMessage);
    }

}
