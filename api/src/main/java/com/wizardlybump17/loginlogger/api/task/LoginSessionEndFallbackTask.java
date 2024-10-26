package com.wizardlybump17.loginlogger.api.task;

import com.wizardlybump17.loginlogger.api.LoginSessionAPI;
import com.wizardlybump17.loginlogger.api.exception.LoginSessionStorageException;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * This class is responsible to keep updating the {@link LoginSession#getEndFallback()} to the current time ({@link Instant#now()})
 * </p>
 *
 * @see LoginSession#getEndFallback()
 */
public abstract class LoginSessionEndFallbackTask implements Runnable {

    private final @NotNull Logger logger;

    public LoginSessionEndFallbackTask(@NotNull Logger logger) {
        this.logger = logger;
    }

    public @NotNull Logger getLogger() {
        return logger;
    }

    /**
     * <p>
     * Executes the update logic. If the {@link LoginSessionStorageException} is thrown, it will be simply logged in the {@link #getLogger()}.
     * </p>
     *
     * @see LoginSession#getEndFallback()
     */
    @Override
    public void run() {
        Instant now = Instant.now();
        LoginSessionStorage storage = LoginSessionAPI.getLoginSessionStorage();
        for (UUID player : getOnlinePlayers()) {
            try {
                LoginSession session = storage.getLatestSession(player);
                if (session == null || session.getEnd() != null)
                    continue;

                session.setEndFallback(now);
                storage.update(session);
            } catch (LoginSessionStorageException e) {
                logger.log(Level.SEVERE, "Error while updating the end fallback for the player " + player, e);
            }
        }
    }

    public abstract @NotNull List<UUID> getOnlinePlayers();
}
