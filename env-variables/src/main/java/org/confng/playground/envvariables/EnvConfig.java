package org.confng.playground.envvariables;

import org.confng.api.ConfNGKey;

/**
 * Environment variables configuration enum.
 * This enum defines all configuration keys used in the environment variables module.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum EnvConfig implements ConfNGKey {
    APP_NAME("app.name", "default-app"),
    APP_VERSION("app.version", "1.0.0"),
    APP_DEBUG("app.debug", "false"),
    
    DATABASE_URL("database.url", "jdbc:h2:mem:testdb"),
    DATABASE_USERNAME("database.username", "sa"),
    DATABASE_PASSWORD("database.password", "", true),
    
    API_KEY("api.key", null, true),
    API_TIMEOUT("api.timeout", "5000");

    private final String key;
    private final String defaultValue;
    private final boolean sensitive;

    EnvConfig(String key, String defaultValue) {
        this(key, defaultValue, false);
    }

    EnvConfig(String key, String defaultValue, boolean sensitive) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.sensitive = sensitive;
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
        return sensitive;
    }
}
