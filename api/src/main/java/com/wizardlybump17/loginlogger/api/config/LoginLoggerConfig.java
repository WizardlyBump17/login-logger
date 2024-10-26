package com.wizardlybump17.loginlogger.api.config;

import com.wizardlybump17.loginlogger.api.session.LoginSession;
import com.wizardlybump17.loginlogger.api.task.LoginSessionEndFallbackTask;

import java.io.IOException;
import java.time.Instant;

public interface LoginLoggerConfig {

    /**
     * @return the milliseconds to be added to {@link Instant#now()} to be set as the {@link LoginSession#getEndFallback()}
     */
    long getInitialEndFallback();

    /**
     * @param fallback the new milliseconds to be added to {@link Instant#now()} to be set as the {@link LoginSession#getEndFallback()}
     */
    void setInitialEndFallback(long fallback);

    /**
     * @return the delay of the {@link LoginSessionEndFallbackTask}
     * @see LoginSessionEndFallbackTask
     */
    int getEndFallbackTaskDelay();

    /**
     * @param delay the new delay of the {@link LoginSessionEndFallbackTask}
     * @see LoginSessionEndFallbackTask
     */
    void setEndFallbackTaskDelay(int delay);

    void save() throws IOException;
}
