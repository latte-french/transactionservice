package utils;

import dataStore.DatabaseCreation;
import dataStore.utils.StatementExecution;
import model.StatementModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class DatabaseCleanup {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializationTest.class);

    public static void prepareDatabase(){
        cleanDatabase();
        DatabaseCreation.initDatabase();
        DatabaseInitializationTest.populateDatabase();
    }

    public static void cleanDatabase() {
        ArrayList<StatementModel> statementModels = new ArrayList();
        try {
            statementModels.add(new StatementModel("DROP TABLE IF EXISTS users"));
            statementModels.add(new StatementModel("DROP TABLE IF EXISTS accounts"));
            statementModels.add(new StatementModel("DROP TABLE IF EXISTS transfers"));
            statementModels.add(new StatementModel("DROP TABLE IF EXISTS user_accounts"));

            StatementExecution.prepareAndExecuteStatements(statementModels);
        }
        catch (Exception e) {
            LOGGER.error("Couldn't cleanup the database");
        }
    }
}
