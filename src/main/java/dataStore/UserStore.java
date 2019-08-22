package dataStore;

import model.User;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserStore {

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

    public static User getUserFromDB(BigInteger id) {
        User user = new User();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM users WHERE id =" + id);
            connection.commit();
            if (result.next()) {user = EntityConverters.convertFromEntityToUser(result);}
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return user;
    }

    public static List<User> getUsersFromDB() {
        List<User> userCollection = new ArrayList<User>();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM users");
            connection.commit();
            while (result.next()){
                userCollection.add(EntityConverters.convertFromEntityToUser(result));
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return userCollection;
    }

    public static void putUserToDB(User user){
        try {
            statement.executeUpdate("INSERT INTO users VALUES (" + user.getId() +
                    "," + user.getFirstName() + ",'" + user.getLastName() + "')");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void removeUserFromDB (BigInteger id){
        try {
            statement.executeUpdate("DELETE FROM users WHERE id =" + id);
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void removeUsersAccountsFromDB(BigInteger id){
        try {
            statement.executeUpdate("DELETE FROM accounts WHERE id in (SELECT account_id FROM user_accounts WHERE user_id =" + id+ ")");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void removeUserAccountDependencyFromDB(BigInteger id){
        try {
            statement.executeUpdate("DELETE FROM user_accounts WHERE user_id =" + id);
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void updateUserInDB(User user){
        BigInteger id = user.getId();
        try {
            if (user.getFirstName() != null){
                statement.executeUpdate("UPDATE users SET first_name = '" + user.getFirstName() +
                        "' WHERE id =" + id); }
            if (user.getLastName() != null){
                statement.executeUpdate("UPDATE users SET last_name = '" + user.getLastName() +
                        "' WHERE id =" + id); }
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
