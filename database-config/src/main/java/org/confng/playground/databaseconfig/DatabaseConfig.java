package org.confng.playground.databaseconfig;

import org.confng.api.ConfNGKey;

/**
 * Database configuration enum for database-related settings.
 * This enum defines all configuration keys used in the database config module.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum DatabaseConfig implements ConfNGKey {
    // Database settings
    DB_POOL_MIN_SIZE("database.pool.min-size", "1"),
    DB_POOL_MAX_SIZE("database.pool.max-size", "10"),
    DB_TIMEOUT("database.timeout", "5000");

    private final String key;
    private final String defaultValue;

    DatabaseConfig(String key, String defaultValue) {
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
