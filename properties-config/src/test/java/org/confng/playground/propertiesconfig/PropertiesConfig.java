package org.confng.playground.propertiesconfig;

import org.confng.api.ConfNGKey;

/**
 * Configuration keys for the Properties Configuration example.
 * 
 * <p>This enum demonstrates various configuration patterns including:</p>
 * <ul>
 *   <li>Application settings</li>
 *   <li>Database configuration</li>
 *   <li>WebDriver settings</li>
 *   <li>Feature flags</li>
 *   <li>Performance tuning</li>
 *   <li>Sensitive data handling</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 */
public enum PropertiesConfig implements ConfNGKey {
    // Application Configuration
    APP_NAME("app.name", "ConfNG Properties Example"),
    APP_VERSION("app.version", "1.0.0"),
    APP_DEBUG("app.debug", "false"),
    APP_PROFILE("app.profile", "development"),
    APP_DESCRIPTION("app.description", null),
    // Database Configuration
    DATABASE_URL("database.url", "jdbc:h2:mem:testdb"),
    DATABASE_USERNAME("database.username", "sa"),
    DATABASE_PASSWORD("database.password", ""),
    DATABASE_DRIVER("database.driver", "org.h2.Driver"),
    DATABASE_POOL_MIN_SIZE("database.pool.min-size", "5"),
    DATABASE_POOL_MAX_SIZE("database.pool.max-size", "20"),
    DATABASE_CONNECTION_TIMEOUT("database.connection-timeout", "30000"),
    DATABASE_VALIDATION_QUERY("database.validation-query", "SELECT 1"),
    DATABASE_POOL_IDLE_TIMEOUT("database.pool.idle-timeout", null),
    DATABASE_POOL_MAX_LIFETIME("database.pool.max-lifetime", null),
    // Database Security
    DATABASE_SSL_ENABLED("database.ssl.enabled", "false"),
    DATABASE_SSL_TRUST_STORE_PATH("database.ssl.trust-store-path", null),
    DATABASE_SSL_TRUST_STORE_PASSWORD("database.ssl.trust-store-password", null),
    // Database Admin Credentials (Sensitive)
    DATABASE_ADMIN_USERNAME("database.admin.username", null, true),
    DATABASE_ADMIN_PASSWORD("database.admin.password", null, true),
    // WebDriver Configuration
    WEBDRIVER_BROWSER("webdriver.browser", "chrome"),
    WEBDRIVER_HEADLESS("webdriver.headless", "false"),
    WEBDRIVER_TIMEOUT("webdriver.timeout", "30"),
    WEBDRIVER_IMPLICIT_WAIT("webdriver.implicit-wait", "10"),
    WEBDRIVER_PAGE_LOAD_TIMEOUT("webdriver.page-load-timeout", "60"),
    // Feature Flags
    FEATURES_NEW_UI("features.new-ui", "false"),
    FEATURES_BETA_API("features.beta-api", "false"),
    FEATURES_ANALYTICS("features.analytics", "false"),
    FEATURES_CACHING("features.caching", "true"),
    // Performance Settings
    CACHE_TTL("cache.ttl", "3600"),
    CACHE_MAX_SIZE("cache.max-size", "1000"),
    THREAD_POOL_SIZE("thread.pool.size", "5"),
    BATCH_SIZE("batch.size", "100"),
    // API Keys and Secrets (Sensitive)
    API_STRIPE_SECRET_KEY("api.stripe.secret-key", null, true),
    API_SENDGRID_API_KEY("api.sendgrid.api-key", null, true),
    OAUTH_CLIENT_SECRET("oauth.client-secret", null, true),
    JWT_SIGNING_KEY("jwt.signing-key", null, true),
    // Service Credentials (Sensitive)
    SERVICE_PAYMENT_API_KEY("service.payment.api-key", null, true),
    SERVICE_ANALYTICS_TOKEN("service.analytics.token", null, true),
    SERVICE_MONITORING_SECRET("service.monitoring.secret", null, true),
    // Encryption Keys (Sensitive)
    ENCRYPTION_MASTER_KEY("encryption.master-key", null, true),
    ENCRYPTION_DATA_KEY("encryption.data-key", null, true);

    private final String key;
    private final String defaultValue;
    private final boolean sensitive;

    PropertiesConfig(String key) {
        this(key, null, false);
    }

    PropertiesConfig(String key, String defaultValue) {
        this(key, defaultValue, false);
    }

    PropertiesConfig(String key, String defaultValue, boolean sensitive) {
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
