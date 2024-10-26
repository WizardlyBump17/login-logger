package com.wizardlybump17.loginlogger.api.storage.sql;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import com.wizardlybump17.loginlogger.api.exception.LoginSessionStorageException;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    /**
     * <p>
     * This class exists because some methods from the {@link LoginSessionStorage} interface conflicts with the {@link Dao} from ORMLite.
     * </p>
     * <p>
     * This class uses the {@link LoginSessionDAO} class to perform SQL queries.
     * </p>
     *
     * @see LoginSessionStorage
     */
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

        @Override
        public @NotNull List<LoginSession> getByPlayer(@NotNull UUID player) throws LoginSessionStorageException {
            try {
                return queryForEq("player", player);
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions from the player " + player, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByPlayerAndGracefulEnd(@NotNull UUID player, boolean gracefulEnd) throws LoginSessionStorageException {
            try {
                return queryForFieldValuesArgs(Map.of(
                        "player", player,
                        "graceful_end", gracefulEnd
                ));
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions from the player " + player + " and graceful end status " + gracefulEnd, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByIp(@NotNull String ip) throws LoginSessionStorageException {
            try {
                return queryForEq("ip", ip);
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions from the ip " + ip, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByLoginBetween(@NotNull Instant start, @NotNull Instant end) throws LoginSessionStorageException {
            try {
                return query(queryBuilder().where().ge("start", start).and().le("start", end).prepare());
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions between " + start + " and " + end, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByLoginAfter(@NotNull Instant after) throws LoginSessionStorageException {
            try {
                return query(queryBuilder().where().ge("start", after).prepare());
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions after " + after, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByLoginBefore(@NotNull Instant before) throws LoginSessionStorageException {
            try {
                return query(queryBuilder().where().le("start", before).prepare());
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions before " + before, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByLogoutBetween(@NotNull Instant start, @NotNull Instant end) throws LoginSessionStorageException {
            try {
                return query(queryBuilder().where().ge("end", start).and().le("end", end).prepare());
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions with logout between " + start + " and " + end, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByLogoutAfter(@NotNull Instant after) throws LoginSessionStorageException {
            try {
                return query(queryBuilder().where().ge("end", after).prepare());
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions with logout after " + after, e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByLogoutBefore(@NotNull Instant before) throws LoginSessionStorageException {
            try {
                return query(queryBuilder().where().le("end", before).prepare());
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions with logout before " + before, e);
            }
        }

        @Override
        public @Nullable LoginSession getLatestSession(@NotNull UUID player) throws LoginSessionStorageException {
            try {
                List<LoginSession> result = query(queryBuilder().limit(1L).orderBy("start", false).where().eq("player", player).prepare());
                return result.isEmpty() ? null : result.get(0);
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the latest login session of " + player, e);
            }
        }

        @Override
        public boolean hasJoinedBefore(@NotNull UUID player) throws LoginSessionStorageException {
            try (GenericRawResults<String[]> results = queryRaw(queryBuilder().limit(1L).selectRaw("1").where().eq("player", player).prepare().getStatement())) {
                String[] first = results.getFirstResult();
                return first != null && first.length == 1 && "1".equals(first[0]);
            } catch (SQLException | IOException e) {
                throw new LoginSessionStorageException("Error while checking if " + player + " has joined before", e);
            }
        }

        @Override
        public @NotNull List<LoginSession> getByGracefulEnd(boolean graceful) throws LoginSessionStorageException {
            try {
                return queryForEq("graceful_end", graceful);
            } catch (SQLException e) {
                throw new LoginSessionStorageException("Error while getting the login sessions with the graceful end status " + graceful, e);
            }
        }
    }
}
