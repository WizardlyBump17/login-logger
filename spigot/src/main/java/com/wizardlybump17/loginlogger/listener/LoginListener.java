package com.wizardlybump17.loginlogger.listener;

import com.wizardlybump17.loginlogger.LoginLoggerPlugin;
import com.wizardlybump17.loginlogger.api.LoginSessionAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public record LoginListener(@NotNull LoginLoggerPlugin plugin) implements Listener {

    public void register() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvent(PlayerJoinEvent.class, this, EventPriority.MONITOR, (listener, event) -> onJoin(((PlayerJoinEvent) event)), plugin);
        manager.registerEvent(PlayerQuitEvent.class, this, EventPriority.MONITOR, (listener, event) -> onQuit(((PlayerQuitEvent) event)), plugin);
    }

    public void onJoin(@NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LoginSessionAPI.getLoginSessionManager().onJoin(player.getUniqueId(), player.getAddress().getHostString());
    }

    public void onQuit(@NotNull PlayerQuitEvent event) {
        LoginSessionAPI.getLoginSessionManager().onQuit(event.getPlayer().getUniqueId());
    }
}
