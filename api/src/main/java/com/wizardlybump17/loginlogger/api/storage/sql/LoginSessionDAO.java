package com.wizardlybump17.loginlogger.api.storage.sql;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.wizardlybump17.loginlogger.api.exception.LoginSessionStorageException;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public class LoginSessionDAO extends BaseDaoImpl<LoginSession, Integer> implements Dao<LoginSession, Integer> {

    private final @NotNull LoginSessionDAO.Storage storage = new Storage();

    public LoginSessionDAO(@NotNull ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, LoginSession.class);
    }

    public @NotNull Storage getStorage() {
        return storage;
    }

    @Override
    public LoginSession createObjectInstance() {
        return new LoginSession();
    }

    //This class exists because some methods cause conflicts with the Dao from ORMLite
    public class Storage implements LoginSessionStorage {

        @Override
        public @NotNull LoginSession store(@NotNull LoginSession session) throws LoginSessionStorageException {
            try {
                return createIfNotExists(session);
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while storing the login session " + session, e);
            }
        }

        @Override
        public @Nullable LoginSession getById(int id) throws LoginSessionStorageException {
            try {
                return queryForId(id);
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login session by the id " + id, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getAll() throws LoginSessionStorageException {
            try {
                return queryForAll();
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting all login sessions", e);
            }
        }

        @Override
        public @NotNull LoginSession update(@NotNull LoginSession session) throws LoginSessionStorageException {
            try {
                LoginSessionDAO.this.update(session);
                return session;
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while updating the login session " + session, e);
            }
        }

        @Override
        public boolean delete(int id) throws LoginSessionStorageException {
            try {
                return deleteById(id) == 1;
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while deleting the login session by the id " + id, e);
            }
        }

        @Override
        public boolean isStored(int id) throws LoginSessionStorageException {
            try {
                return idExists(id);
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while checking if the login session by the id " + id + " exists", e);
            }
        }
    }
}
