package dataStore;

import model.Account;
import model.Transfer;
import model.User;

import java.math.BigInteger;
import java.sql.ResultSet;


public class EntityConverters {


    protected static Account convertFromEntityToAccount(ResultSet result){
        Account account = new Account();
        try {
            account.setId(new BigInteger(result.getString("id")));
            account.setBalance(result.getDouble("balance"));
            account.setCurrency(result.getString("currency"));
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return account;
    }

    protected static User convertFromEntityToUser(ResultSet result){
        User user = new User();
        try {
            user.setId(new BigInteger(result.getString("id")));
            user.setFirstName(result.getString("first_name"));
            user.setLastName(result.getString("last_name"));
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return user;
    }

    protected static Transfer convertFromEntityToTransfer(ResultSet result){
        Transfer transfer = new Transfer();
        try {
            transfer.setAccountFromId(new BigInteger(result.getString("account_from")));
            transfer.setAccountToId(new BigInteger(result.getString("account_to")));
            transfer.setSumToTransfer(result.getDouble("sum_from"));
            transfer.setSumTransferred(result.getDouble("sum_to"));
            transfer.setTransferredAt(result.getTimestamp("transferred_at"));
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return transfer;
    }
}
