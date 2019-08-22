package dataStore;

import model.Account;
import model.Transfer;
import model.exceptions.NoSuchAccountException;
import service.AccountService;
import service.impl.AccountServiceImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransferStore {

    private static TransferStore instance;
    private static Connection connection;
    private static Statement statement;
    private final AccountService accountService = AccountServiceImpl.getInstance();
    private final EntityConverters entityConverters = EntityConverters.getInstance();

    static{
        try{
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:transaction_service", "admin", "");
            statement = connection.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static TransferStore getInstance() {
        if (instance == null) {
            instance = new TransferStore();
        }
        return instance;
    }
    public void putTransferToDB(Transfer transfer) throws NoSuchAccountException {
        Account accountFrom = accountService.getAccount(transfer.getAccountFromId());
        Account accountTo = accountService.getAccount(transfer.getAccountToId());;
        try {
            statement.executeUpdate("INSERT INTO transfers VALUES (null, " +
                    accountFrom.getId() + "," + transfer.getSumToTransfer() + ",'" + accountFrom.getCurrency() + "'," +
                    accountTo.getId()+ "," + transfer.getSumTransferred() + ",'" + accountTo.getCurrency() + "','" +
                    transfer.getTransferredAt() +"')");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public List<Transfer> getTransfersFromDB(){
        List<Transfer> transferCollection = new ArrayList<Transfer>();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM transfers");
            connection.commit();
            while (result.next()){
                transferCollection.add(entityConverters.convertFromEntityToTransfer(result));
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return transferCollection;
    }

}
