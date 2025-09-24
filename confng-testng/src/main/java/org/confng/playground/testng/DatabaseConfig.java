package org.confng.playground.testng;

import org.confng.api.ConfNGKey;

/**
 * Database configuration enum for TestNG examples.
 * This enum defines database-related configuration keys.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0.1
 * @since 2025
 */
public enum DatabaseConfig implements ConfNGKey {
    DB_URL("database.url", "jdbc:h2:mem:testdb"),
    DB_USERNAME("database.username", "sa"),
    DB_PASSWORD("database.password", ""),
    DB_POOL_SIZE("database.poolSize", "5"),
    DB_TIMEOUT("database.timeout", "30000");

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
        return this == DB_PASSWORD;
    }
}
