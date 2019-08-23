package dataStore;

import model.StatementModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

public class StatementExecution {

    private static Connection connection;
    private static PreparedStatement statement;
    private static Savepoint transactionSavepoint;
    private static String statementMessage;
    private static ArrayList<String> statementObjects;
    private static ArrayList<PreparedStatement> statements;
    private static ResultSet result;
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementExecution.class);

    static{
        try{
            connection = DriverManager.getConnection("jdbc:hsqldb:mem:transaction_service", "admin", "");
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static ResultSet prepareAndExecuteQuery (StatementModel statementModel) throws SQLException {
       statement = prepareStatementForExecution(statementModel);
       return queryExecution(statement);
    }

    public static void prepareAndExecuteStatement (StatementModel statementModel) throws SQLException {
        statement = prepareStatementForExecution(statementModel);
        statementExecution(statement);
    }

    public static void prepareAndExecuteStatements (ArrayList<StatementModel> statementModels) throws SQLException {
        statements = new ArrayList<>();
        Iterator<StatementModel> statementModelsIterator = statementModels.iterator();

        while (statementModelsIterator.hasNext()) {
            statements.add(prepareStatementForExecution(statementModelsIterator.next()));
        }

        statementsExecution(statements);
    }

    public static PreparedStatement prepareStatementForExecution (StatementModel statementModel) throws SQLException {
        statementMessage = statementModel.getStatementMessage();
        statementObjects = statementModel.getStatementObjects();

        statement = connection.prepareStatement(statementMessage);
        Iterator<String> statementObjectsIterator = statementObjects.iterator();

        while (statementObjectsIterator.hasNext()) {
            String nextObject = statementObjectsIterator.next();
            int indexPos = statementObjects.indexOf(nextObject) + 1;
            statement.setString(indexPos, nextObject);
        }
        return statement;
    }

    public static void statementExecution (PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
        connection.commit();
    }

    public static void statementsExecution (ArrayList<PreparedStatement> statements) throws SQLException {
       try {
           Iterator<PreparedStatement> PreparedStatementIterator = statements.iterator();
           while (PreparedStatementIterator.hasNext()) {
               PreparedStatementIterator.next().executeUpdate();
           }
       } catch (SQLException e) {
           connection.rollback ();
           throw new SQLException(e);
       }
        connection.commit();
    }

    public static ResultSet queryExecution (PreparedStatement statement) throws SQLException {
        result = statement.executeQuery();
        connection.commit();
        return result;
    }
}
