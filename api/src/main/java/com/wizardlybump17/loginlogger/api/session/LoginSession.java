package com.wizardlybump17.loginlogger.api.session;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class LoginSession {

    private Integer id;
    private @NotNull UUID player;
    private @NotNull String ip;
    private @NotNull Instant start;
    private @Nullable Instant end;
    private boolean joinedBefore;

    public LoginSession(@NotNull UUID player, @NotNull String ip, @NotNull Instant start, boolean joinedBefore) {
        this.player = player;
        this.ip = ip;
        this.start = start;
        this.joinedBefore = joinedBefore;
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

    public boolean hasJoinedBefore() {
        return joinedBefore;
    }

    public void setJoinedBefore(boolean joinedBefore) {
        this.joinedBefore = joinedBefore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoginSession that = (LoginSession) o;
        return joinedBefore == that.joinedBefore
                && Objects.equals(id, that.id)
                && Objects.equals(player, that.player)
                && Objects.equals(ip, that.ip)
                && Objects.equals(start, that.start)
                && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player, ip, start, end, joinedBefore);
    }

    @Override
    public String toString() {
        return "LoginSession{" +
                "id=" + id +
                ", player=" + player +
                ", ip='" + ip + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", joinedBefore=" + joinedBefore +
                '}';
    }
}
