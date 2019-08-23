package dataStore;

import model.StatementModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class DatabaseInitialization {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitialization.class);
    private static ArrayList<StatementModel> statementModels;

    public static void populateDatabase(){

        populateUserTable();
        populateAccountTable();
        populateUserAccountTable();

    }


    public static void populateUserTable() {
        statementModels = new ArrayList<>();
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
        statementModels = new ArrayList<>();
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
        statementModels = new ArrayList<>();
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
}
