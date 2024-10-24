package com.wizardlybump17.loginlogger.test.util;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import org.jetbrains.annotations.NotNull;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import java.io.IOException;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static DatabaseConnection instance;

    private ConnectionSource connectionSource;
    private JdbcDatabaseContainer<?> databaseContainer;

    private DatabaseConnection() {
    }

    public void open() throws SQLException {
        assertNotConnected();

        databaseContainer = createDatabaseContainer();
        databaseContainer.start();

        connectionSource = new JdbcPooledConnectionSource(
                databaseContainer.getJdbcUrl(),
                databaseContainer.getUsername(),
                databaseContainer.getPassword()
        );

        TableUtils.createTableIfNotExists(connectionSource, LoginSession.class);
    }

    private @NotNull JdbcDatabaseContainer<?> createDatabaseContainer() {
        return new MySQLContainer<>("mysql");
    }

    public void close() throws IOException {
        assertConnected();

        connectionSource.close();

        databaseContainer.stop();

        connectionSource = null;
        databaseContainer = null;
    }

    public boolean isOpen() {
        return connectionSource != null && databaseContainer != null;
    }

    private void assertConnected() {
        if (!isOpen())
            throw new IllegalStateException("The database is not connected");
    }

    private void assertNotConnected() {
        if (isOpen())
            throw new IllegalStateException("The database is connected");
    }

    public static DatabaseConnection getInstance() {
        return instance == null ? instance = new DatabaseConnection() : instance;
    }

    public <D extends Dao<T, ?>, T> @NotNull D getDao(@NotNull Class<T> type) {
        assertConnected();
        return DaoManager.lookupDao(connectionSource, type);
    }
}
