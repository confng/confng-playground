package org.confng.playground.jsonconfig;

import org.confng.api.ConfNGKey;

/**
 * Application configuration enum for JSON configuration settings.
 * This enum defines configuration keys for core application parameters and settings.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum AppConfig implements ConfNGKey {
    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    APP_DEBUG("app.debug"),
    
    DB_URL("database.url"),
    DB_USERNAME("database.username"),
    DB_PASSWORD("database.password"),
    DB_MAX_CONNECTIONS("database.maxConnections"),
    
    WEBDRIVER_BROWSER("webdriver.browser"),
    WEBDRIVER_HEADLESS("webdriver.headless"),
    WEBDRIVER_TIMEOUT("webdriver.timeout"),
    WEBDRIVER_IMPLICIT_WAIT("webdriver.implicitWait");

    private final String key;

    AppConfig(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDefaultValue() {
        return null;
    }

    @Override
    public boolean isSensitive() {
        return this == DB_PASSWORD;
    }
}