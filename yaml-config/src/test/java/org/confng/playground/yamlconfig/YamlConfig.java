package org.confng.playground.yamlconfig;

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for the YAML Configuration example.
 * 
 * <p>This enum demonstrates accessing nested YAML configuration using dot notation:</p>
 * <ul>
 *   <li>Simple properties: app.name, app.version</li>
 *   <li>Nested objects: database.primary.url, api.services.payment.baseUrl</li>
 *   <li>Arrays: app.features (as comma-separated string)</li>
 *   <li>Complex structures: webdriver.capabilities.chrome.switches</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 */
public enum YamlConfig implements ConfNGKey {
    
    // Application Configuration
    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    APP_ENVIRONMENT("app.environment"),
    APP_DEBUG("app.debug"),
    APP_DESCRIPTION("app.description"),
    
    // Database Configuration - Primary
    DATABASE_PRIMARY_URL("database.primary.url"),
    DATABASE_PRIMARY_USERNAME("database.primary.username"),
    DATABASE_PRIMARY_PASSWORD("database.primary.password", null, true),
    DATABASE_PRIMARY_DRIVER("database.primary.driver"),
    DATABASE_PRIMARY_POOL_MAX_SIZE("database.primary.pool.maxSize"),
    DATABASE_PRIMARY_POOL_MIN_SIZE("database.primary.pool.minSize"),
    DATABASE_PRIMARY_POOL_TIMEOUT("database.primary.pool.timeout"),
    DATABASE_PRIMARY_POOL_IDLE_TIMEOUT("database.primary.pool.idleTimeout"),
    
    // Database Configuration - Secondary
    DATABASE_SECONDARY_URL("database.secondary.url"),
    DATABASE_SECONDARY_USERNAME("database.secondary.username"),
    DATABASE_SECONDARY_PASSWORD("database.secondary.password", null, true),
    DATABASE_SECONDARY_POOL_MAX_SIZE("database.secondary.pool.maxSize"),
    DATABASE_SECONDARY_POOL_MIN_SIZE("database.secondary.pool.minSize"),
    DATABASE_SECONDARY_POOL_TIMEOUT("database.secondary.pool.timeout"),
    
    // WebDriver Configuration
    WEBDRIVER_BROWSER("webdriver.browser"),
    WEBDRIVER_HEADLESS("webdriver.headless"),
    WEBDRIVER_TIMEOUT("webdriver.timeout"),
    WEBDRIVER_IMPLICIT_WAIT("webdriver.implicitWait"),
    WEBDRIVER_PAGE_LOAD_TIMEOUT("webdriver.pageLoadTimeout"),
    
    // API Services Configuration
    API_PAYMENT_BASE_URL("api.services.service1.baseUrl"),
    API_PAYMENT_TIMEOUT("api.services.service1.timeout"),
    API_PAYMENT_RETRY_ATTEMPTS("api.services.service1.retryAttempts"),
    API_PAYMENT_API_VERSION("api.services.service1.apiVersion"),

    API_NOTIFICATION_BASE_URL("api.services.service2.baseUrl"),
    API_NOTIFICATION_TIMEOUT("api.services.service2.timeout"),
    API_NOTIFICATION_RETRY_ATTEMPTS("api.services.service2.retryAttempts"),
    API_NOTIFICATION_API_VERSION("api.services.service2.apiVersion"),

    API_ANALYTICS_BASE_URL("api.services.service3.baseUrl"),
    API_ANALYTICS_TIMEOUT("api.services.service3.timeout"),
    API_ANALYTICS_RETRY_ATTEMPTS("api.services.service3.retryAttempts"),
    API_ANALYTICS_BATCH_SIZE("api.services.service3.batchSize"),
    
    // Monitoring Configuration
    MONITORING_METRICS_ENABLED("monitoring.metrics.enabled"),
    MONITORING_METRICS_INTERVAL("monitoring.metrics.interval"),
    MONITORING_LOGGING_LEVEL("monitoring.logging.level"),
    
    // Cache Configuration
    CACHE_REDIS_HOST("cache.redis.host"),
    CACHE_REDIS_PORT("cache.redis.port"),
    CACHE_REDIS_DATABASE("cache.redis.database"),
    CACHE_REDIS_TIMEOUT("cache.redis.timeout"),
    CACHE_REDIS_POOL_MAX_ACTIVE("cache.redis.pool.maxActive"),
    CACHE_REDIS_POOL_MAX_IDLE("cache.redis.pool.maxIdle"),
    CACHE_REDIS_POOL_MIN_IDLE("cache.redis.pool.minIdle"),
    
    CACHE_LOCAL_MAX_SIZE("cache.local.maxSize"),
    CACHE_LOCAL_TTL("cache.local.ttl"),
    CACHE_LOCAL_EVICTION_POLICY("cache.local.evictionPolicy"),
    
    // Security Configuration
    SECURITY_JWT_SECRET("security.jwt.secret", null, true),
    SECURITY_JWT_EXPIRATION("security.jwt.expiration"),
    SECURITY_JWT_ISSUER("security.jwt.issuer"),
    
    SECURITY_OAUTH_PROVIDER1_CLIENT_ID("security.oauth.providers.provider1.clientId"),
    SECURITY_OAUTH_PROVIDER1_CLIENT_SECRET("security.oauth.providers.provider1.clientSecret", null, true),
    SECURITY_OAUTH_PROVIDER1_REDIRECT_URI("security.oauth.providers.provider1.redirectUri"),

    SECURITY_OAUTH_PROVIDER2_CLIENT_ID("security.oauth.providers.provider2.clientId"),
    SECURITY_OAUTH_PROVIDER2_CLIENT_SECRET("security.oauth.providers.provider2.clientSecret", null, true),
    SECURITY_OAUTH_PROVIDER2_REDIRECT_URI("security.oauth.providers.provider2.redirectUri");
    
    private final String key;
    private final String defaultValue;
    private final boolean sensitive;
    
    YamlConfig(String key) {
        this(key, null, false);
    }
    
    YamlConfig(String key, String defaultValue) {
        this(key, defaultValue, false);
    }
    
    YamlConfig(String key, String defaultValue, boolean sensitive) {
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
        return switch (this) {
            case APP_NAME -> "Default YAML Application";
            case APP_VERSION -> "0.0.1";
            case APP_ENVIRONMENT -> "unknown";
            case APP_DEBUG -> "false";
            case DATABASE_PRIMARY_URL -> "jdbc:h2:mem:primary";
            case DATABASE_PRIMARY_USERNAME -> "sa";
            case DATABASE_PRIMARY_PASSWORD -> "";
            case DATABASE_PRIMARY_DRIVER -> "org.h2.Driver";
            case DATABASE_PRIMARY_POOL_MAX_SIZE -> "10";
            case DATABASE_PRIMARY_POOL_MIN_SIZE -> "2";
            case DATABASE_PRIMARY_POOL_TIMEOUT -> "30000";
            case DATABASE_SECONDARY_URL -> "jdbc:h2:mem:secondary";
            case DATABASE_SECONDARY_USERNAME -> "sa";
            case DATABASE_SECONDARY_POOL_MAX_SIZE -> "5";
            case DATABASE_SECONDARY_POOL_MIN_SIZE -> "1";
            case WEBDRIVER_BROWSER -> "chrome";
            case WEBDRIVER_HEADLESS -> "false";
            case WEBDRIVER_TIMEOUT -> "30";
            case WEBDRIVER_IMPLICIT_WAIT -> "10";
            case WEBDRIVER_PAGE_LOAD_TIMEOUT -> "60";
            case API_PAYMENT_BASE_URL -> "https://api.default.com";
            case API_PAYMENT_TIMEOUT -> "10000";
            case API_PAYMENT_RETRY_ATTEMPTS -> "1";
            case API_NOTIFICATION_BASE_URL -> "https://notification.default.com";
            case API_NOTIFICATION_TIMEOUT -> "5000";
            case API_NOTIFICATION_RETRY_ATTEMPTS -> "1";
            case MONITORING_METRICS_ENABLED -> "false";
            case MONITORING_METRICS_INTERVAL -> "60000";
            case MONITORING_LOGGING_LEVEL -> "WARN";
            case CACHE_REDIS_HOST -> "localhost";
            case CACHE_REDIS_PORT -> "6379";
            case CACHE_REDIS_DATABASE -> "0";
            case CACHE_REDIS_TIMEOUT -> "3000";
            case CACHE_LOCAL_MAX_SIZE -> "100";
            case CACHE_LOCAL_TTL -> "3600";
            case CACHE_LOCAL_EVICTION_POLICY -> "LRU";
            case SECURITY_JWT_EXPIRATION -> "3600";
            case SECURITY_JWT_ISSUER -> "default-issuer";
            default -> defaultValue;
        };
    }
    
    @Override
    public boolean isSensitive() {
        return sensitive;
    }
}
