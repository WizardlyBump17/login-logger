package com.wizardlybump17.loginlogger.api;

import com.wizardlybump17.loginlogger.api.config.LoginLoggerConfig;
import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import org.jetbrains.annotations.NotNull;

public final class LoginSessionAPI {

    private static LoginSessionStorage loginSessionStorage;
    private static LoginLoggerConfig config;

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
}
