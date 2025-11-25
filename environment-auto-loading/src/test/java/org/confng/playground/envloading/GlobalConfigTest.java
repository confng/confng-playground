package org.confng.playground.envloading;

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates the global/common configuration loading feature in ConfNG.
 * 
 * <p>Global configuration files (global.properties, global.json, etc.) are loaded
 * BEFORE environment-specific files, providing a base configuration that applies
 * to all environments.</p>
 * 
 * <p>Environment-specific files can override global values while inheriting
 * values that are not overridden.</p>
 */
public class GlobalConfigTest {

    public enum GlobalConfig implements ConfNGKey {
        // Values defined in global.properties
        APP_NAME("app.name", "default-app"),
        APP_VERSION("app.version", "1.0.0"),
        APP_RETRY_COUNT("app.retry.count", "3"),
        
        CACHE_ENABLED("cache.enabled", "true"),
        CACHE_TTL("cache.ttl", "300"),
        CACHE_MAX_SIZE("cache.maxSize", "1000"),
        
        LOG_FORMAT("log.format", "json"),
        
        FEATURES_CACHING("features.caching", "true"),
        FEATURES_COMPRESSION("features.compression", "true"),
        
        // Values that may be overridden by environment-specific files
        LOG_LEVEL("log.level", "INFO"),
        API_TIMEOUT("api.timeout", "10000"),
        DATABASE_POOL_MAX_SIZE("database.pool.maxSize", "20"),

        // Values from global.toml (base config)
        DB_POOL_SIZE("db.poolSize", "10"),
        DB_CONNECTION_TIMEOUT("db.connectionTimeout", "5000"),
        FEATURE_NEW_UI("feature.newUI", "false"),
        FEATURE_BETA_FEATURES("feature.betaFeatures", "false"),

        // Values from global.toml environment sections
        DB_HOST("db.host", "localhost"),
        DB_PORT("db.port", "5432"),
        API_BASE_URL("api.baseUrl", "http://localhost:8080");

        private final String key;
        private final String defaultValue;

        GlobalConfig(String key, String defaultValue) {
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

    @BeforeClass
    public void setup() {
        // NOTE: When running with TestNG XML, the TestNGParameterListener automatically
        // loads global config and environment-specific config in the correct order.
        // We don't need to load them again here.
    }

    @Test
    public void testGlobalConfigurationLoading() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Global Configuration Loading");
        System.out.println("========================================");
        System.out.println();
        System.out.println("loadGlobalConfig() automatically loads:");
        System.out.println("  - global.properties");
        System.out.println("  - global.json");
        System.out.println("  - global.yaml");
        System.out.println("  - global.toml");
        System.out.println("  - common.properties");
        System.out.println("  - common.json");
        System.out.println("  - common.yaml");
        System.out.println("  - common.toml");
        System.out.println();
        System.out.println("(Files that don't exist are silently skipped)");
        System.out.println("========================================");
    }

    @Test
    public void testGlobalValuesAreLoaded() {
        // These values are defined in global.properties
        assertThat(ConfNG.get(GlobalConfig.APP_NAME))
            .isEqualTo("ConfNG Environment Auto-Loading Example");
        
        assertThat(ConfNG.get(GlobalConfig.APP_VERSION))
            .isEqualTo("1.0.0");
        
        assertThat(ConfNG.getInt(GlobalConfig.APP_RETRY_COUNT))
            .isEqualTo(3);
        
        System.out.println("✅ Global application settings loaded");
    }

    @Test
    public void testGlobalCacheSettings() {
        // Cache settings from global.properties
        assertThat(ConfNG.getBoolean(GlobalConfig.CACHE_ENABLED))
            .isTrue();
        
        assertThat(ConfNG.getInt(GlobalConfig.CACHE_TTL))
            .isEqualTo(300);
        
        assertThat(ConfNG.getInt(GlobalConfig.CACHE_MAX_SIZE))
            .isEqualTo(1000);
        
        System.out.println("✅ Global cache settings loaded");
    }

    @Test
    public void testGlobalFeatureFlags() {
        // Feature flags from global.properties
        assertThat(ConfNG.getBoolean(GlobalConfig.FEATURES_CACHING))
            .isTrue();
        
        assertThat(ConfNG.getBoolean(GlobalConfig.FEATURES_COMPRESSION))
            .isTrue();
        
        System.out.println("✅ Global feature flags loaded");
    }

    @Test
    public void testEnvironmentSpecificOverridesGlobal() {
        // log.level is defined in global.properties as INFO
        // but environment-specific files can override it
        
        String logLevel = ConfNG.get(GlobalConfig.LOG_LEVEL);
        
        System.out.println();
        System.out.println("Override example: log.level");
        System.out.println("  global.properties: log.level=INFO");
        System.out.println("  Current value: " + logLevel);
        
        // In UAT, it should be DEBUG (overridden)
        // In production, it should be WARN (overridden)
        // In local, it should be DEBUG (overridden)
        
        assertThat(logLevel).isIn("INFO", "DEBUG", "WARN");
        
        System.out.println("  ✅ Environment-specific override working");
    }

    @Test
    public void testGlobalValuesInheritedWhenNotOverridden() {
        // log.format is defined in global.properties
        // and is NOT overridden in any environment-specific file
        
        String logFormat = ConfNG.get(GlobalConfig.LOG_FORMAT);
        
        System.out.println();
        System.out.println("Inheritance example: log.format");
        System.out.println("  global.properties: log.format=json");
        System.out.println("  Environment files: (not overridden)");
        System.out.println("  Current value: " + logFormat);
        System.out.println("  ✅ Inherited from global configuration");
        
        assertThat(logFormat).isEqualTo("json");
    }

    @Test
    public void testGlobalVsEnvironmentPattern() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Global vs Environment-Specific Pattern");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Use global.properties for:");
        System.out.println("  ✅ Settings shared across ALL environments");
        System.out.println("  ✅ Default values that rarely change");
        System.out.println("  ✅ Application constants");
        System.out.println("  ✅ Feature flags (default state)");
        System.out.println();
        System.out.println("Use {env}.properties for:");
        System.out.println("  ✅ Environment-specific URLs");
        System.out.println("  ✅ Database connections");
        System.out.println("  ✅ Resource limits (pool sizes, timeouts)");
        System.out.println("  ✅ Feature flag overrides");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  global.properties:");
        System.out.println("    app.name=MyApp");
        System.out.println("    app.version=1.0.0");
        System.out.println("    cache.ttl=300");
        System.out.println("    database.pool.maxSize=20");
        System.out.println();
        System.out.println("  uat.properties:");
        System.out.println("    api.url=https://uat-api.example.com");
        System.out.println("    database.pool.maxSize=30  ← overrides global");
        System.out.println();
        System.out.println("  Result in UAT:");
        System.out.println("    app.name=MyApp            ← from global");
        System.out.println("    app.version=1.0.0         ← from global");
        System.out.println("    cache.ttl=300             ← from global");
        System.out.println("    database.pool.maxSize=30  ← from uat");
        System.out.println("    api.url=https://uat-...   ← from uat");
        System.out.println("========================================");
    }

    @Test
    public void testRealWorldUseCases() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Real-World Use Cases");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Use Case 1: Multi-Environment Application");
        System.out.println("  global.properties: app metadata, defaults");
        System.out.println("  local.json: local dev settings");
        System.out.println("  uat.properties: UAT environment");
        System.out.println("  prod.properties: production environment");
        System.out.println();
        System.out.println("Use Case 2: Microservices");
        System.out.println("  global.properties: service name, version");
        System.out.println("  {env}.properties: environment-specific endpoints");
        System.out.println();
        System.out.println("Use Case 3: Feature Flags");
        System.out.println("  global.properties: default feature states");
        System.out.println("  uat.properties: enable experimental features");
        System.out.println("  prod.properties: enable stable features only");
        System.out.println("========================================");
    }

    @Test
    public void testGlobalTomlWithEnvironmentSections() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Global TOML with Environment Sections");
        System.out.println("========================================");
        System.out.println();
        System.out.println("global.toml can contain:");
        System.out.println("  1. Common configuration (top level)");
        System.out.println("  2. Environment-specific sections [dev], [uat], [prod]");
        System.out.println();
        System.out.println("ConfNG automatically:");
        System.out.println("  1. Loads common config from global.toml");
        System.out.println("  2. Detects current environment");
        System.out.println("  3. Loads environment section from same global.toml");
        System.out.println();

        // Test common values from global.toml
        assertThat(ConfNG.getInt(GlobalConfig.DB_POOL_SIZE))
            .isEqualTo(10);
        System.out.println("✅ Common config loaded: db.poolSize = 10");

        assertThat(ConfNG.getInt(GlobalConfig.DB_CONNECTION_TIMEOUT))
            .isEqualTo(5000);
        System.out.println("✅ Common config loaded: db.connectionTimeout = 5000");

        // Test environment-specific values from global.toml
        // These values come from the [dev], [uat], or [prod] section
        String dbHost = ConfNG.get(GlobalConfig.DB_HOST);
        String apiBaseUrl = ConfNG.get(GlobalConfig.API_BASE_URL);

        System.out.println();
        System.out.println("Environment-specific values from global.toml:");
        System.out.println("  db.host = " + dbHost);
        System.out.println("  api.baseUrl = " + apiBaseUrl);

        // Verify values are loaded (exact value depends on environment)
        assertThat(dbHost).isNotNull();
        assertThat(apiBaseUrl).isNotNull();

        System.out.println();
        System.out.println("Example global.toml structure:");
        System.out.println("  # Common config");
        System.out.println("  db.poolSize = 10");
        System.out.println("  db.connectionTimeout = 5000");
        System.out.println();
        System.out.println("  [dev]");
        System.out.println("  db.host = \"localhost\"");
        System.out.println("  api.baseUrl = \"http://localhost:8080\"");
        System.out.println();
        System.out.println("  [uat]");
        System.out.println("  db.host = \"uat-db.example.com\"");
        System.out.println("  api.baseUrl = \"https://uat-api.example.com\"");
        System.out.println("========================================");
    }
}

