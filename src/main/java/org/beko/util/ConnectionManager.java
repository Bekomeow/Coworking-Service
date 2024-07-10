package org.beko.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

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
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class ConnectionManager {
    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.driver-class-name}")
    private String driver;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;
    @Value("${datasource.pool-size}")
    private int poolSize;
    private BlockingQueue<Connection> pool;
    private List<Connection> sourceConnection;

    public ConnectionManager() {
        loadDriver();
        initConnectionPool(
                url,
                username,
                password);
    }

    public ConnectionManager(String url, String username, String password) {
        loadDriver();
        initConnectionPool(url, username, password);
    }

    /**
     * Initializes the connection pool.
     */
    private void initConnectionPool(String url, String username, String password) {
        System.out.println("poolSize " + poolSize);

        pool = new ArrayBlockingQueue<>(poolSize);
        sourceConnection = new ArrayList<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
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

