package org.beko.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * A singleton class to handle Liquibase database migrations.
 */
@Slf4j
public class LiquibaseDemo {
    private static final LiquibaseDemo liquibaseDemo = new LiquibaseDemo();
    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";

    private LiquibaseDemo() {
        // private constructor to enforce singleton pattern
    }

    /**
     * Runs the Liquibase migrations using the given database connection.
     *
     * @param connection the database connection
     */
    public void runMigrations(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA)) {
            preparedStatement.execute();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName("migration");

            Liquibase liquibase = new Liquibase("db/changelog/main-changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            log.info("Migrations completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the singleton instance of the LiquibaseDemo class.
     *
     * @return the singleton instance
     */
    public static LiquibaseDemo getInstance() {
        return liquibaseDemo;
    }
}