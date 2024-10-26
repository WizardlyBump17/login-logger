package com.wizardlybump17.loginlogger.config;

import com.wizardlybump17.loginlogger.api.config.LoginLoggerConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class YamlLoginLoggerConfig implements LoginLoggerConfig {

    public static final @NotNull String INITIAL_END_FALLBACK = "end-fallback.initial";
    public static final @NotNull String END_FALLBACK_TASK_DELAY = "end-fallback.task-delay";

    private final @NotNull YamlConfiguration configuration;
    private final @NotNull File file;

    public YamlLoginLoggerConfig(@NotNull YamlConfiguration configuration, @NotNull File file) {
        this.configuration = configuration;
        this.file = file;
    }

    public @NotNull YamlConfiguration getConfiguration() {
        return configuration;
    }

    public @NotNull File getFile() {
        return file;
    }

    @Override
    public long getInitialEndFallback() {
        return configuration.getLong(INITIAL_END_FALLBACK);
    }

    @Override
    public void setInitialEndFallback(long fallback) {
        configuration.set(INITIAL_END_FALLBACK, fallback);
    }

    @Override
    public int getEndFallbackTaskDelay() {
        return configuration.getInt(END_FALLBACK_TASK_DELAY);
    }

    @Override
    public void setEndFallbackTaskDelay(int delay) {
        configuration.set(END_FALLBACK_TASK_DELAY, delay);
    }

    @Override
    public void save() throws IOException {
        configuration.save(file);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        YamlLoginLoggerConfig that = (YamlLoginLoggerConfig) o;
        return Objects.equals(configuration, that.configuration)
                && Objects.equals(file, that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configuration, file);
    }

    @Override
    public String toString() {
        return "YamlLoginLoggerConfig{" +
                "configuration=" + configuration +
                ", file=" + file +
                '}';
    }
}
