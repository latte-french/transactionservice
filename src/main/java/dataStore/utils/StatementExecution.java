package dataStore.utils;

import dataStore.ConnectionPool;
import model.StatementModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class StatementExecution {

    private static PreparedStatement statement;
    private static String statementMessage;
    private static ArrayList<String> statementObjects;
    private static ArrayList<PreparedStatement> statements;
    private static ResultSet result;
    private static ConnectionPool connectionPool = new ConnectionPool();
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementExecution.class);

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
        statement = getConnectionFromPool().prepareStatement(statementMessage);
        //connectionPool.printDbStatus();

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
        Connection connection = getConnectionFromPool();
        connection.commit();
        closeConnectionInPool(connection);
    }

    public static void statementsExecution (ArrayList<PreparedStatement> statements) throws SQLException {
        try {
            Iterator<PreparedStatement> PreparedStatementIterator = statements.iterator();
            while (PreparedStatementIterator.hasNext()) {
                PreparedStatementIterator.next().executeUpdate();
            }
        } catch (SQLException e) {
            getConnectionFromPool().rollback ();
            throw new SQLException(e);
        }
        Connection connection = getConnectionFromPool();
        connection.commit();
        closeConnectionInPool(connection);
    }

    public static ResultSet queryExecution (PreparedStatement statement) throws SQLException {
        result = statement.executeQuery();
        Connection connection = getConnectionFromPool();
        connection.commit();
        closeConnectionInPool(connection);
        return result;
    }

    public static Connection getConnectionFromPool(){
        Connection connection = null;
        try{
            DataSource dataSource = connectionPool.setUpPool();
            // connectionPool.printDbStatus();
            connection = dataSource.getConnection();
            // connectionPool.printDbStatus();
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        return connection;
    }

    public static void closeConnectionInPool(Connection connection){
        try {
            if(connection != null) {
                connection.close();
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        // connectionPool.printDbStatus();
    }
}
