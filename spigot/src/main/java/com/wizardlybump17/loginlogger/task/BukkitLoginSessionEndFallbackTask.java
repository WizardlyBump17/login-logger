package com.wizardlybump17.loginlogger.task;

import com.wizardlybump17.loginlogger.api.task.LoginSessionEndFallbackTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BukkitLoginSessionEndFallbackTask extends LoginSessionEndFallbackTask {

    public BukkitLoginSessionEndFallbackTask(@NotNull Logger logger) {
        super(logger);
    }

    @Override
    public @NotNull List<UUID> getOnlinePlayers() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getUniqueId)
                .toList();
    }
}
