package org.confng.playground.jsonconfig;

import org.confng.api.ConfNGKey;

/**
 * Configuration class for database settings using ConfNG framework.
 * This enum defines configuration keys for database connection and operations.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum DatabaseConfig implements ConfNGKey {
    DB_URL("database.url"),
    DB_USERNAME("database.username"),
    DB_PASSWORD("database.password"),
    DB_MAX_CONNECTIONS("database.maxConnections");

    private final String key;

    DatabaseConfig(String key) {
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
