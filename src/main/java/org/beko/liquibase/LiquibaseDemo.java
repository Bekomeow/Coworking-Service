package org.beko.liquibase;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * A singleton class to handle Liquibase database migrations.
 */
@Slf4j
@AllArgsConstructor
public class LiquibaseDemo {
    private static final String SQL_CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS migration";
    private final Connection connection;
    private final String changeLogFile;
    private final String schemaName;

    /**
     * Runs database migrations using Liquibase.
     */
    public void runMigrations() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_CREATE_SCHEMA)) {
            preparedStatement.execute();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            database.setLiquibaseSchemaName(schemaName);

            Liquibase liquibase = new Liquibase(changeLogFile , new ClassLoaderResourceAccessor(), database);
            liquibase.update();

            log.info("Migrations completed successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}