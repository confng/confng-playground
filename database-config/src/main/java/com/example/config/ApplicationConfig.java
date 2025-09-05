package com.example.config;

/**
 * Configuration class for application settings using ConfNG framework.
 * This enum defines configuration keys for core application parameters.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for application settings
 */
public enum ApplicationConfig implements ConfNGKey {
    APP_NAME("app.name", "Default App"),
    APP_VERSION("app.version", "1.0.0"),
    APP_DEBUG("app.debug", "false");

    private final String key;
    private final String defaultValue;

    ApplicationConfig(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isSensitive() {
        return false;
    }
}