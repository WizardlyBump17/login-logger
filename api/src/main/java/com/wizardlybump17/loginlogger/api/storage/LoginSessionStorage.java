package com.wizardlybump17.loginlogger.api.storage;

import com.wizardlybump17.loginlogger.api.exception.LoginSessionStorageException;
import com.wizardlybump17.loginlogger.api.session.LoginSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface LoginSessionStorage {

    @NotNull LoginSession store(@NotNull LoginSession session) throws LoginSessionStorageException;

    @Nullable LoginSession getById(int id) throws LoginSessionStorageException;

    @NotNull List<LoginSession> getAll() throws LoginSessionStorageException;

    @NotNull LoginSession update(@NotNull LoginSession session) throws LoginSessionStorageException;

    boolean delete(int id) throws LoginSessionStorageException;

    default boolean delete(@NotNull LoginSession session) throws LoginSessionStorageException {
        return delete(session.getId());
    }

    boolean isStored(int id) throws LoginSessionStorageException;

    default boolean isStored(@NotNull LoginSession session) throws LoginSessionStorageException {
        return isStored(session.getId());
    }

    @NotNull List<LoginSession> getByPlayer(@NotNull UUID player) throws LoginSessionStorageException;

    @NotNull List<LoginSession> getByIp(@NotNull String ip) throws LoginSessionStorageException;

    @NotNull List<LoginSession> getByJoinedBefore(boolean joinedBefore) throws LoginSessionStorageException;

    @NotNull List<LoginSession> getByLoginBetween(@NotNull Instant start, @NotNull Instant end) throws LoginSessionStorageException;

    @NotNull List<LoginSession> getByLoginAfter(@NotNull Instant after) throws LoginSessionStorageException;

    @NotNull List<LoginSession> getByLoginBefore(@NotNull Instant before) throws LoginSessionStorageException;
}
