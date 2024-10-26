package com.wizardlybump17.loginlogger.api.manager;

import com.wizardlybump17.loginlogger.api.LoginSessionAPI;
import com.wizardlybump17.loginlogger.api.exception.LoginSessionStorageException;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginSessionManager {

    private final @NotNull Logger logger;

    public LoginSessionManager(@NotNull Logger logger) {
        this.logger = logger;
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    /**
     * <p>
     * Performs the "graceful end" and storing logics.
     * </p>
     * <p>
     * First it tries to assign the {@link LoginSession#getEnd()} to the {@link LoginSession#getEndFallback()} to
     * any {@link LoginSession}s that returns {@code false} for {@link LoginSession#isGracefulEnd()}.
     * </p>
     * <p>
     * After that is done, this method creates a new {@link LoginSession} and stores it in the {@link LoginSessionStorage}
     * provided by {@link LoginSessionAPI#getLoginSessionStorage()}.
     * </p>
     * <p>
     * Any {@link LoginSessionStorageException} thrown is simply logged at {@link #getLogger()}
     * </p>
     *
     * @param player the player who is joining
     * @param ip     the ip of the player
     * @return if the method successfully stored the {@link LoginSession}
     * @see LoginSessionStorage#getByPlayerAndGracefulEnd(UUID, boolean)
     * @see LoginSessionStorage#store(LoginSession)
     */
    public boolean onJoin(@NotNull UUID player, @NotNull String ip) {
        LoginSessionStorage storage = LoginSessionAPI.getLoginSessionStorage();

        try {
            for (LoginSession session : storage.getByPlayerAndGracefulEnd(player, false)) {
                session.setEnd(session.getEndFallback());
                storage.update(session);
            }
        } catch (LoginSessionStorageException e) {
            logger.log(Level.SEVERE, "Error while handling the end fallback for the player " + player);
        }

        try {
            Instant now = Instant.now();
            storage.store(new LoginSession(
                    player,
                    ip,
                    now,
                    now.plus(LoginSessionAPI.getConfig().getInitialEndFallback(), ChronoUnit.MILLIS)
            ));
            return true;
        } catch (LoginSessionStorageException e) {
            logger.log(Level.SEVERE, "Error while creating a login session for " + player, e);
            return false;
        }
    }

    /**
     * <p>
     * This method will attempt to end the latest {@link LoginSession} of the given player and mark it as "gracefully ended".
     * </p>
     * <p>
     * If a {@link LoginSession} is not found or if it already has an end (i.e. {@link LoginSession#getEnd()} != {@code null}),
     * it will be simply logged and this method will return {@code false}.
     * </p>
     * <p>
     * Any {@link LoginSessionStorageException} thrown is simply logged at {@link #getLogger()}
     * </p>
     *
     * @param player the player who is quitting
     * @return if the {@link LoginSession} was successfully ended
     * @see LoginSessionStorage#getLatestSession(UUID)
     * @see LoginSession#getEnd()
     */
    public boolean onQuit(@NotNull UUID player) {
        LoginSessionStorage storage = LoginSessionAPI.getLoginSessionStorage();

        try {
            LoginSession session = storage.getLatestSession(player);
            if (session == null) {
                logger.severe(player + " does not have a login session.");
                return false;
            }

            if (session.getEnd() != null) {
                logger.severe("The latest login session (" + session.getId() + ") from " + player + " already have a logout time.");
                return false;
            }

            session.setEnd(Instant.now());
            session.setGracefulEnd(true);
            storage.update(session);

            return true;
        } catch (LoginSessionStorageException e) {
            logger.log(Level.SEVERE, "Error while handling the logout of " + player, e);
            return false;
        }
    }
}
