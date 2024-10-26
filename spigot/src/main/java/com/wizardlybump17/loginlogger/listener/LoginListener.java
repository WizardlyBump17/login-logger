package com.wizardlybump17.loginlogger.listener;

import com.wizardlybump17.loginlogger.LoginLoggerPlugin;
import com.wizardlybump17.loginlogger.api.LoginSessionAPI;
import com.wizardlybump17.loginlogger.api.exception.LoginSessionStorageException;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public record LoginListener(@NotNull LoginLoggerPlugin plugin) implements Listener {

    public void register() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvent(PlayerJoinEvent.class, this, EventPriority.LOWEST, (listener, event) -> onJoin(((PlayerJoinEvent) event)), plugin);
        manager.registerEvent(PlayerQuitEvent.class, this, EventPriority.LOWEST, (listener, event) -> onQuit(((PlayerQuitEvent) event)), plugin);
    }

    public void onJoin(@NotNull PlayerJoinEvent event) {
        LoginSessionStorage storage = LoginSessionAPI.getLoginSessionStorage();
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        Logger logger = plugin.getLogger();

        try {
            for (LoginSession session : storage.getByPlayerAndGracefulEnd(id, false)) {
                session.setEnd(session.getEndFallback());
                storage.update(session);
            }
        } catch (LoginSessionStorageException e) {
            logger.log(Level.SEVERE, "Error while handling the end fallback for the player " + id);
        }

        try {
            Instant now = Instant.now();
            storage.store(new LoginSession(
                    id,
                    player.getAddress().getHostString(),
                    now,
                    now.plus(LoginSessionAPI.getConfig().getInitialEndFallback(), ChronoUnit.MILLIS)
            ));
        } catch (LoginSessionStorageException e) {
            logger.log(Level.SEVERE, "Error while creating a login session for " + id, e);
        }
    }

    public void onQuit(@NotNull PlayerQuitEvent event) {
        LoginSessionStorage storage = LoginSessionAPI.getLoginSessionStorage();
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        try {
            LoginSession session = storage.getLatestSession(id);
            if (session == null) {
                plugin.getLogger().severe(id + " does not have a login session.");
                return;
            }

            if (session.getEnd() != null) {
                plugin.getLogger().severe("The latest login session (" + session.getId() + ") from " + id + " already have a logout time.");
                return;
            }

            session.setEnd(Instant.now());
            session.setGracefulEnd(true);
            storage.update(session);
        } catch (LoginSessionStorageException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while handling the logout of " + id, e);
        }
    }
}
