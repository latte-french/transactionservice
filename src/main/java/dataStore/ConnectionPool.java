package dataStore;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.sql.DataSource;

public class ConnectionPool {

    static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    static final String JDBC_DB_URL = "jdbc:hsqldb:mem:transaction_service";

    // JDBC Database Credentials
    static final String JDBC_USER = "admin";
    static final String JDBC_PASS = "admin";

    private static GenericObjectPool genericObjectPoolPool = null;

    public DataSource setUpPool() throws Exception {
        Class.forName(JDBC_DRIVER);

        genericObjectPoolPool = new GenericObjectPool();
        genericObjectPoolPool.setMaxActive(1000);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);


        PoolableConnectionFactory pcf;

        try {
            pcf = new PoolableConnectionFactory(connectionFactory, genericObjectPoolPool,
                    null, null, false, true);

        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return new PoolingDataSource(genericObjectPoolPool);
    }

    public GenericObjectPool getConnectionPool() {
        return genericObjectPoolPool;
    }

    // This Method Is Used To Print The Connection Pool Status
    public void printDbStatus() {
        System.out.println("Max.: " + getConnectionPool().getMaxActive() + "; Active: " +
                getConnectionPool().getNumActive() + "; Idle: " + getConnectionPool().getNumIdle());
    }
}
