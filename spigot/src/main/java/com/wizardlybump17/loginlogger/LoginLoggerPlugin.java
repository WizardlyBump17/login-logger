package com.wizardlybump17.loginlogger;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class LoginLoggerPlugin extends JavaPlugin {

    public static @NotNull LoginLoggerPlugin getInstance() {
        return getPlugin(LoginLoggerPlugin.class);
    }
}
