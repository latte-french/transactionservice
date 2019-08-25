package utils;

import dataStore.utils.StatementExecution;
import model.StatementModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class DatabaseInitializationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializationTest.class);

    public static void populateDatabase(){

        populateUserTable();
        populateAccountTable();
        populateUserAccountTable();
        populateTransfersTable();

    }


    public static void populateUserTable() {
        ArrayList<StatementModel> statementModels = new ArrayList();
        try {
            statementModels.add(new StatementModel("INSERT INTO users VALUES (1,'Alex', 'Smith')"));
            statementModels.add(new StatementModel("INSERT INTO users VALUES (2,'Clint', 'Eastwood')"));
            statementModels.add(new StatementModel("INSERT INTO users VALUES (3,'Peter', 'Pan')"));
            StatementExecution.prepareAndExecuteStatements(statementModels);
        }
        catch (Exception e) {
            LOGGER.error("Couldn't populate the users table in database");
        }
    }

    public static void populateAccountTable(){
        ArrayList<StatementModel> statementModels = new ArrayList();
        try {
            statementModels.add(new StatementModel("INSERT INTO accounts VALUES (4000123412341234,23.56, 'RUB')"));
            statementModels.add(new StatementModel("INSERT INTO accounts VALUES (4000123412341235,5.8, 'USD')"));
            statementModels.add(new StatementModel("INSERT INTO accounts VALUES (4000123412341236,102.13, 'EUR')"));
            StatementExecution.prepareAndExecuteStatements(statementModels);
        }
        catch (Exception e) {
            LOGGER.error("Couldn't populate the accounts table in database");
        }
    }

    public static void populateUserAccountTable(){
        ArrayList<StatementModel> statementModels = new ArrayList();
        try {
            statementModels.add(new StatementModel("INSERT INTO user_accounts VALUES (null,1,4000123412341234)"));
            statementModels.add(new StatementModel("INSERT INTO user_accounts VALUES (null,2,4000123412341235)"));
            statementModels.add(new StatementModel("INSERT INTO user_accounts VALUES (null,2,4000123412341236)"));
            StatementExecution.prepareAndExecuteStatements(statementModels);
        }
        catch (Exception e) {
            LOGGER.error("Couldn't populate the user_accounts table in database");
        }
    }

    public static void populateTransfersTable(){
        ArrayList<StatementModel> statementModels = new ArrayList();
        try{
            statementModels.add(new StatementModel("INSERT INTO transfers VALUES (null," +
                    " 4000123412341234, 3.0, 'RUB', 4000123412341235, 6.7, 'EUR'," +
                    "TIMESTAMP '2019-08-24 05:03:04.697')"));
            statementModels.add(new StatementModel("INSERT INTO transfers VALUES (null, " +
                    "4000123412341235, 21.7, 'USD', 4000123412341236, 378.123, 'GBP', " +
                    "TIMESTAMP '2019-08-24 06:03:04.697')"));
            StatementExecution.prepareAndExecuteStatements(statementModels);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
