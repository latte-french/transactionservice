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

    private static final ConnectionPool connectionPool = new ConnectionPool();
    private static final Logger LOGGER = LoggerFactory.getLogger(StatementExecution.class);


    public static ResultSet prepareAndExecuteQuery (StatementModel statementModel) throws SQLException {
        PreparedStatement statement = prepareStatementForExecution(statementModel);
        return queryExecution(statement);
    }

    public static void prepareAndExecuteStatement (StatementModel statementModel) throws SQLException {
        PreparedStatement statement = prepareStatementForExecution(statementModel);
        statementExecution(statement);
    }

    public static void prepareAndExecuteStatements (ArrayList<StatementModel> statementModels) throws SQLException {
        ArrayList<PreparedStatement> statements = new ArrayList<>();
        Iterator<StatementModel> statementModelsIterator = statementModels.iterator();

        while (statementModelsIterator.hasNext()) {
            statements.add(prepareStatementForExecution(statementModelsIterator.next()));
        }
        statementsExecution(statements);
    }

    public static synchronized PreparedStatement prepareStatementForExecution (StatementModel statementModel) throws SQLException {
        String statementMessage = statementModel.getStatementMessage();
        ArrayList<String> statementObjects = statementModel.getStatementObjects();
        Connection connection = getConnectionFromPool();
        PreparedStatement statement = connection.prepareStatement(statementMessage);
        //connectionPool.printDbStatus();

        Iterator<String> statementObjectsIterator = statementObjects.iterator();

        while (statementObjectsIterator.hasNext()) {
            String nextObject = statementObjectsIterator.next();
            int indexPos = statementObjects.indexOf(nextObject) + 1;
            statement.setString(indexPos, nextObject);
        }
        return statement;
    }

    public static synchronized void statementExecution (PreparedStatement statement) throws SQLException {

        statement.executeUpdate();
        Connection connection = getConnectionFromPool();
        connection.commit();
        closeConnectionInPool(connection);
    }

    public static synchronized void statementsExecution (ArrayList<PreparedStatement> statements) throws SQLException {
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

    public static synchronized ResultSet queryExecution (PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        Connection connection = getConnectionFromPool();
        connection.commit();
        closeConnectionInPool(connection);
        return result;
    }

    public static synchronized Connection getConnectionFromPool(){
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

    public static synchronized void closeConnectionInPool(Connection connection){
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
