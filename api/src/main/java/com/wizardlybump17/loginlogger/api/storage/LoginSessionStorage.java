package com.wizardlybump17.loginlogger.api.storage;

import com.wizardlybump17.loginlogger.api.LoginSession;
import com.wizardlybump17.loginlogger.api.exception.LoginSessionStorageException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface LoginSessionStorage {

    @NotNull LoginSession store(@NotNull LoginSession session)throws LoginSessionStorageException;

    @Nullable LoginSession getById(int id)throws LoginSessionStorageException;

    @NotNull List<LoginSession> getAll()throws LoginSessionStorageException;

    @NotNull LoginSession update(@NotNull LoginSession session)throws LoginSessionStorageException;

    boolean delete(int id)throws LoginSessionStorageException;

    default boolean delete(@NotNull LoginSession session)throws LoginSessionStorageException {
        return delete(session.getId());
    }

    boolean isStored(int id)throws LoginSessionStorageException;

    default boolean isStored(@NotNull LoginSession session)throws LoginSessionStorageException {
        return isStored(session.getId());
    }
}
