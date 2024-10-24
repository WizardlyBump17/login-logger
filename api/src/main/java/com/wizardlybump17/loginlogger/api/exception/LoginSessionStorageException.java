package com.wizardlybump17.loginlogger.api.exception;

import org.jetbrains.annotations.NotNull;

public class LoginSessionStorageException extends Exception {

    public LoginSessionStorageException() {
    }

    public LoginSessionStorageException(@NotNull String message) {
        super(message);
    }

    public LoginSessionStorageException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }

    public LoginSessionStorageException(@NotNull Throwable cause) {
        super(cause);
    }
}
