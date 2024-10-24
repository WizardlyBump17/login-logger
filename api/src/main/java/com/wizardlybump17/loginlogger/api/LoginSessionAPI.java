package com.wizardlybump17.loginlogger.api;

import com.wizardlybump17.loginlogger.api.storage.LoginSessionStorage;
import org.jetbrains.annotations.NotNull;

public final class LoginSessionAPI {

    private static LoginSessionStorage loginSessionStorage;

    private LoginSessionAPI() {
    }

    public static void setLoginSessionStorage(@NotNull LoginSessionStorage loginSessionStorage) {
        LoginSessionAPI.loginSessionStorage = loginSessionStorage;
    }

    public static @NotNull LoginSessionStorage getLoginSessionStorage() {
        return loginSessionStorage;
    }
}
