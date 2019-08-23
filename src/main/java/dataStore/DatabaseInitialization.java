package dataStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitialization {
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

    public static void initDatabase(){

        populateUserTable();
        populateAccountTable();
        populateUserAccountTable();

    }


    public static void populateUserTable() {
        try {
            statement.executeUpdate("INSERT INTO users VALUES (1,'Alex', 'Smith')");
            statement.executeUpdate("INSERT INTO users VALUES (2,'Clint', 'Eastwood')");
            statement.executeUpdate("INSERT INTO users VALUES (3,'Peter', 'Pan')");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void populateAccountTable(){
        try {
            statement.executeUpdate("INSERT INTO accounts VALUES (4000123412341234,23.56, 'RUB')");
            statement.executeUpdate("INSERT INTO accounts VALUES (4000123412341235,5.8, 'USD')");
            statement.executeUpdate("INSERT INTO accounts VALUES (4000123412341236,102.13, 'EUR')");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void populateUserAccountTable(){
        try {
            statement.executeUpdate("INSERT INTO user_accounts VALUES (null,1,4000123412341234)");
            statement.executeUpdate("INSERT INTO user_accounts VALUES (null,2,4000123412341235)");
            statement.executeUpdate("INSERT INTO user_accounts VALUES (null,2,4000123412341236)");
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
