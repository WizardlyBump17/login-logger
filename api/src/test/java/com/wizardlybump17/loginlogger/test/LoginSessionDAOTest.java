package com.wizardlybump17.loginlogger.test;

import com.wizardlybump17.loginlogger.api.LoginSessionAPI;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.storage.sql.LoginSessionDAO;
import com.wizardlybump17.loginlogger.test.util.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.SQLException;

class LoginSessionDAOTest {

    @BeforeAll
    static void setup() throws SQLException {
        DatabaseConnection connection = DatabaseConnection.getInstance();
        connection.open();

        LoginSessionDAO sessionDAO = connection.getDao(LoginSession.class);
        LoginSessionAPI.setLoginSessionStorage(sessionDAO.getStorage());
    }

    @AfterAll
    static void shutdown() throws IOException {
        DatabaseConnection.getInstance().close();
    }
}
