package com.wizardlybump17.loginlogger.api.session;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.wizardlybump17.loginlogger.api.config.LoginLoggerConfig;
import com.wizardlybump17.loginlogger.api.persister.InstantType;
import com.wizardlybump17.loginlogger.api.storage.sql.LoginSessionDAO;
import com.wizardlybump17.loginlogger.api.task.LoginSessionEndFallbackTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@DatabaseTable(tableName = "login_logger_login_session", daoClass = LoginSessionDAO.class)
public class LoginSession {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    private Integer id;
    @DatabaseField(columnName = "player", width = 36, canBeNull = false)
    private @NotNull UUID player;
    @DatabaseField(columnName = "ip", canBeNull = false)
    private @NotNull String ip;
    @DatabaseField(columnName = "start", canBeNull = false, persisterClass = InstantType.class)
    private @NotNull Instant start;
    @DatabaseField(columnName = "end", persisterClass = InstantType.class)
    private @Nullable Instant end;
    @DatabaseField(columnName = "end_fallback", canBeNull = false, persisterClass = InstantType.class)
    private @NotNull Instant endFallback;
    @DatabaseField(columnName = "graceful_end", canBeNull = false)
    private boolean gracefulEnd;

    public LoginSession(@NotNull UUID player, @NotNull String ip, @NotNull Instant start, @NotNull Instant endFallback) {
        this.player = player;
        this.ip = ip;
        this.start = start;
        this.endFallback = endFallback;
    }

    public LoginSession() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotNull UUID getPlayer() {
        return player;
    }

    public void setPlayer(@NotNull UUID player) {
        this.player = player;
    }

    public @NotNull String getIp() {
        return ip;
    }

    public void setIp(@NotNull String ip) {
        this.ip = ip;
    }

    public @NotNull Instant getStart() {
        return start;
    }

    public void setStart(@NotNull Instant start) {
        this.start = start;
    }

    public @Nullable Instant getEnd() {
        return end;
    }

    public void setEnd(@Nullable Instant end) {
        this.end = end;
    }

    /**
     * <p>
     * The "end fallback" system is for when the player unexpectedly gets disconnected without a chance for the logger system
     * to take note. E.g. this can happen when the server is killed.
     * </p>
     * <p>
     * This system holds the last time the player was seen online and in case the server is killed, when the player joins
     * again, the {@link #getEnd()} will be set to the value returned by this method.
     * </p>
     * <p>
     * When the player joins, this value is set to {@link Instant#now()} + {@link LoginLoggerConfig#getInitialEndFallback()},
     * and it is updated to the current time in accord of {@link LoginLoggerConfig#getEndFallbackTaskDelay()}.
     * </p>
     *
     * @return the last time the player was seen online
     * @see LoginLoggerConfig#getInitialEndFallback()
     * @see LoginLoggerConfig#getEndFallbackTaskDelay()
     * @see LoginSessionEndFallbackTask
     */
    public @NotNull Instant getEndFallback() {
        return endFallback;
    }

    /**
     * <p>
     * The "end fallback" system is for when the player unexpectedly gets disconnected without a chance for the logger system
     * to take note. E.g. this can happen when the server is killed.
     * </p>
     * <p>
     * This system holds the last time the player was seen online and in case the server is killed, when the player joins
     * again, the {@link #getEnd()} will be set to the value returned by this method.
     * </p>
     * <p>
     * When the player joins, this value is set to {@link Instant#now()} + {@link LoginLoggerConfig#getInitialEndFallback()},
     * and it is updated to the current time in accord of {@link LoginLoggerConfig#getEndFallbackTaskDelay()}.
     * </p>
     *
     * @param endFallback the last time the player was seen online
     * @see LoginLoggerConfig#getInitialEndFallback()
     * @see LoginLoggerConfig#getEndFallbackTaskDelay()
     * @see LoginSessionEndFallbackTask
     */
    public void setEndFallback(@NotNull Instant endFallback) {
        this.endFallback = endFallback;
    }

    /**
     * <p>
     * The "graceful end" system is for when the player unexpectedly gets disconnected without a chance for the logger system
     * to take note. E.g. this can happen when the server is killed.
     * </p>
     *
     * @return if the player disconnected from the server normally
     * @see #getEndFallback()
     */
    public boolean isGracefulEnd() {
        return gracefulEnd;
    }

    /**
     * <p>
     * The "graceful end" system is for when the player unexpectedly gets disconnected without a chance for the logger system
     * to take note. E.g. this can happen when the server is killed.
     * </p>
     *
     * @param gracefulEnd if the player disconnected from the server normally
     * @see #getEndFallback()
     */
    public void setGracefulEnd(boolean gracefulEnd) {
        this.gracefulEnd = gracefulEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoginSession that = (LoginSession) o;
        return gracefulEnd == that.gracefulEnd
                && Objects.equals(id, that.id)
                && Objects.equals(player, that.player)
                && Objects.equals(ip, that.ip)
                && Objects.equals(start, that.start)
                && Objects.equals(end, that.end)
                && Objects.equals(endFallback, that.endFallback);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player, ip, start, end, endFallback, gracefulEnd);
    }

    @Override
    public String toString() {
        return "LoginSession{" +
                "id=" + id +
                ", player=" + player +
                ", ip='" + ip + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", endFallback=" + endFallback +
                ", gracefulEnd=" + gracefulEnd +
                '}';
    }
}
