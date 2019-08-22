package dataStore;

import model.Account;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AccountStore {

    private static Connection connection;
    private static Statement statement;

    static{
        try{
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:transaction_service", "admin", "");
            statement = connection.createStatement();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static Account getAccountFromDB(BigInteger id) {
        Account account = new Account();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM accounts WHERE id =" + id);
            connection.commit();
            if (result.next()) {account = EntityConverters.convertFromEntityToAccount(result);}
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return account;
    }

    public static List<Account> getAccountsFromDB() {
        List<Account> accountCollection = new ArrayList<Account>();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM accounts");
            connection.commit();
            while (result.next()){
                 accountCollection.add(EntityConverters.convertFromEntityToAccount(result));
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return accountCollection;
    }

    public static List<Account> getAccountsOfUserFromDB(BigInteger id) {
        ResultSet result = null;
        List<Account> accountCollection = new ArrayList<Account>();
        try {
            result = statement.executeQuery("SELECT * FROM accounts WHERE id IN "+
                    "(SELECT account_id from user_accounts where user_id =" + id + ")");
            connection.commit();
            while (result.next()){
                accountCollection.add(EntityConverters.convertFromEntityToAccount(result));
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return accountCollection;
    }

    public static void putAccountToDB(Account account){
        try {
            statement.executeUpdate("INSERT INTO accounts VALUES (" + account.getId() +
                    "," + account.getBalance() + ",'" + account.getCurrency() + "')");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void putUserAccountDependencyToDB(BigInteger accountId, BigInteger userId){
        try {
            statement.executeUpdate("INSERT INTO user_accounts VALUES (null," + userId + "," + accountId + ")");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void removeAccountFromDB (BigInteger id){
        try {
            statement.executeUpdate("DELETE FROM accounts WHERE id =" + id);
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void removeUserAccountDependencyFromDB(BigInteger id){
        try {
            statement.executeUpdate("DELETE FROM user_accounts WHERE account_id =" + id);
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void updateAccountInDB(Account account){
        BigInteger id = account.getId();
        try {
            if (account.getBalance() != null){
                statement.executeUpdate("UPDATE accounts SET balance = " + account.getBalance() +
                                "WHERE id =" + id); }
            if (account.getCurrency() != null){
                statement.executeUpdate("UPDATE accounts SET currency = '" + account.getCurrency() +
                        "' WHERE id =" + id); }
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

}