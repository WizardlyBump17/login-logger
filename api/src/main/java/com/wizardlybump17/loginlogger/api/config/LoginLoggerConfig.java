package com.wizardlybump17.loginlogger.api.config;

import java.io.IOException;

public interface LoginLoggerConfig {

    long getInitialEndFallback();

    void setInitialEndFallback(long fallback);

    int getEndFallbackTaskDelay();

    void setEndFallbackTaskDelay(int delay);

    void save() throws IOException;
}
