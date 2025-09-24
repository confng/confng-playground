package org.confng.playground.multisource;

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for the Multi-Source Configuration example.
 * 
 * <p>This enum demonstrates configuration keys that can be resolved from multiple sources
 * with different precedence levels:</p>
 * <ul>
 *   <li>Environment Variables (highest precedence)</li>
 *   <li>System Properties</li>
 *   <li>JSON Configuration</li>
 *   <li>Properties Files</li>
 *   <li>Default Values (lowest precedence)</li>
 * </ul>
 */
public enum MultiSourceConfig implements ConfNGKey {
    
    // Application Configuration
    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    APP_ENVIRONMENT("app.environment"),
    APP_TIMEOUT("app.timeout"),
    APP_DESCRIPTION("app.description"),
    APP_RETRY_ATTEMPTS("app.retry.attempts"),
    
    // Database Configuration
    DATABASE_URL("database.url"),
    DATABASE_USERNAME("database.username"),
    DATABASE_PASSWORD("database.password"),
    DATABASE_POOL_MAX_SIZE("database.pool.maxSize"),
    DATABASE_POOL_MIN_SIZE("database.pool.minSize"),
    DATABASE_POOL_TIMEOUT("database.pool.timeout"),
    
    // Feature Flags
    FEATURES_NEW_UI("features.newUI"),
    FEATURES_ANALYTICS("features.analytics"),
    FEATURES_CACHING("features.caching"),
    FEATURES_MONITORING("features.monitoring"),
    
    // WebDriver Configuration
    WEBDRIVER_BROWSER("webdriver.browser"),
    WEBDRIVER_HEADLESS("webdriver.headless"),
    WEBDRIVER_TIMEOUT("webdriver.timeout"),
    
    // API Configuration
    API_BASE_URL("api.baseUrl"),
    API_TIMEOUT("api.timeout"),
    API_RETRY_ATTEMPTS("api.retryAttempts"),
    
    // Properties-only Configuration
    LOGGING_LEVEL("logging.level"),
    LOGGING_FILE("logging.file"),
    CACHE_SIZE("cache.size"),
    THREAD_POOL_SIZE("thread.pool.size"),
    
    // Test Configuration
    TEST_PRECEDENCE("test.precedence"),
    CUSTOM_VALUE("custom.value");
    
    private final String key;
    
    MultiSourceConfig(String key) {
        this.key = key;
    }
    
    @Override
    public String getKey() {
        return key;
    }
    
    @Override
    public String getDefaultValue() {
        return switch (this) {
            case APP_NAME -> "Default ConfNG Application";
            case APP_VERSION -> "0.0.1";
            case APP_ENVIRONMENT -> "unknown";
            case APP_TIMEOUT -> "10000";
            case APP_RETRY_ATTEMPTS -> "1";
            case DATABASE_URL -> "jdbc:h2:mem:defaultdb";
            case DATABASE_USERNAME -> "default_user";
            case DATABASE_PASSWORD -> "";
            case DATABASE_POOL_MAX_SIZE -> "5";
            case DATABASE_POOL_MIN_SIZE -> "1";
            case DATABASE_POOL_TIMEOUT -> "15000";
            case FEATURES_NEW_UI -> "false";
            case FEATURES_ANALYTICS -> "false";
            case FEATURES_CACHING -> "false";
            case FEATURES_MONITORING -> "false";
            case WEBDRIVER_BROWSER -> "chrome";
            case WEBDRIVER_HEADLESS -> "false";
            case WEBDRIVER_TIMEOUT -> "20";
            case API_BASE_URL -> "https://default.example.com";
            case API_TIMEOUT -> "5000";
            case API_RETRY_ATTEMPTS -> "1";
            case LOGGING_LEVEL -> "WARN";
            case CACHE_SIZE -> "100";
            case THREAD_POOL_SIZE -> "1";
            case TEST_PRECEDENCE -> "default-value";
            case CUSTOM_VALUE -> "default-custom";
            default -> null;
        };
    }
    
    @Override
    public boolean isSensitive() {
        return this == DATABASE_PASSWORD;
    }
}