package com.wizardlybump17.loginlogger.test;

import com.wizardlybump17.loginlogger.test.util.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.SQLException;

class LoginSessionDAOTest {

    @BeforeAll
    static void setupConnectionSource() throws SQLException {
        DatabaseConnection.getInstance().open();
    }

    @AfterAll
    static void shutdown() throws IOException {
        DatabaseConnection.getInstance().close();
    }
}
