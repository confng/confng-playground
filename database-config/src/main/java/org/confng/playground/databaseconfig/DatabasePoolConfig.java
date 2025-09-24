package org.confng.playground.databaseconfig;

import org.confng.api.ConfNGKey;

/**
 * Configuration class for database pool settings using ConfNG framework.
 * This enum defines configuration keys for database connection pool parameters.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum DatabasePoolConfig implements ConfNGKey {
    DB_POOL_MIN_SIZE("database.pool.min-size", "1"),
    DB_POOL_MAX_SIZE("database.pool.max-size", "10"),
    DB_TIMEOUT("database.timeout", "5000");

    private final String key;
    private final String defaultValue;

    DatabasePoolConfig(String key, String defaultValue) {
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
