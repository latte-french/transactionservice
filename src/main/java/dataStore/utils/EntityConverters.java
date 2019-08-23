package dataStore.utils;

import model.Account;
import model.Transfer;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.ResultSet;


public class EntityConverters {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityConverters.class);


    public static Account convertFromEntityToAccount(ResultSet result){
        Account account = new Account();
        try {
            account.setId(new BigInteger(result.getString("id")));
            account.setBalance(result.getDouble("balance"));
            account.setCurrency(result.getString("currency"));
        }
        catch (Exception e) {
            LOGGER.error("Couldn't convert entity to account");
        }
        return account;
    }

    public static User convertFromEntityToUser(ResultSet result){
        User user = new User();
        try {
            user.setId(new BigInteger(result.getString("id")));
            user.setFirstName(result.getString("first_name"));
            user.setLastName(result.getString("last_name"));
        }
        catch (Exception e) {
            LOGGER.error("Couldn't convert entity to user");
        }
        return user;
    }

    public static Transfer convertFromEntityToTransfer(ResultSet result){
        Transfer transfer = new Transfer();
        try {
            transfer.setAccountFromId(new BigInteger(result.getString("account_from")));
            transfer.setAccountToId(new BigInteger(result.getString("account_to")));
            transfer.setSumToTransfer(result.getDouble("sum_from"));
            transfer.setSumTransferred(result.getDouble("sum_to"));
            transfer.setTransferredAt(result.getTimestamp("transferred_at"));
        }
        catch (Exception e) {
            LOGGER.error("Couldn't convert entity to transfer");
        }
        return transfer;
    }
}
