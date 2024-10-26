package com.wizardlybump17.loginlogger.api;

import com.wizardlybump17.loginlogger.api.config.LoginLoggerConfig;
import com.wizardlybump17.loginlogger.api.manager.LoginSessionManager;
import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import com.wizardlybump17.loginlogger.api.task.LoginSessionEndFallbackTask;
import org.jetbrains.annotations.NotNull;

public final class LoginSessionAPI {

    private static LoginSessionStorage loginSessionStorage;
    private static LoginLoggerConfig config;
    private static LoginSessionEndFallbackTask endFallbackTask;
    private static LoginSessionManager loginSessionManager;

    private LoginSessionAPI() {
    }

    public static void setLoginSessionStorage(@NotNull LoginSessionStorage loginSessionStorage) {
        LoginSessionAPI.loginSessionStorage = loginSessionStorage;
    }

    public static @NotNull LoginSessionStorage getLoginSessionStorage() {
        return loginSessionStorage;
    }

    public static LoginLoggerConfig getConfig() {
        return config;
    }

    public static void setConfig(@NotNull LoginLoggerConfig config) {
        LoginSessionAPI.config = config;
    }

    public static LoginSessionEndFallbackTask getEndFallbackTask() {
        return endFallbackTask;
    }

    public static void setEndFallbackTask(@NotNull LoginSessionEndFallbackTask endFallbackTask) {
        LoginSessionAPI.endFallbackTask = endFallbackTask;
    }

    public static LoginSessionManager getLoginSessionManager() {
        return loginSessionManager;
    }

    public static void setLoginSessionManager(LoginSessionManager loginSessionManager) {
        LoginSessionAPI.loginSessionManager = loginSessionManager;
    }
}
