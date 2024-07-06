package org.beko.util;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Manages database connections using a connection pool.
 */
public class ConnectionManager {
    private static final String PASSWORD_KEY = "db.password";
    private static final String USERNAME_KEY = "db.username";
    private static final String URl_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final int DEFAULT_POOL_SIZE = 5;
    private BlockingQueue<Connection> pool;
    private List<Connection> sourceConnection;

    public ConnectionManager() {
        loadDriver();
        initConnectionPool(
                PropertiesUtil.get(URl_KEY),
                PropertiesUtil.get(USERNAME_KEY),
                PropertiesUtil.get(PASSWORD_KEY));
    }

    public ConnectionManager(String url, String username, String password) {
        loadDriver();
        initConnectionPool(url, username, password);
    }

    /**
     * Initializes the connection pool.
     */
    private void initConnectionPool(String url, String username, String password) {
        var poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        var size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnection = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            var connection = open(url, username, password);
            var proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, objects) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy) : method.invoke(connection, objects));
            pool.add(proxyConnection);
            sourceConnection.add(connection);
        }
    }

    /**
     * Gets a connection from the pool.
     *
     * @return a database connection
     */
    public Connection getConnection() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the database driver.
     */
    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens a new database connection.
     *
     * @return a new database connection
     */
    public Connection open(String url, String username, String password) {
        try {
            return DriverManager.getConnection(
                    url,
                    username,
                    password
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes all connections in the pool.
     */
    public void closePool() {
        for (Connection connection:sourceConnection) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

