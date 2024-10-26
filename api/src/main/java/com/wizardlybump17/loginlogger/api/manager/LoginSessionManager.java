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
