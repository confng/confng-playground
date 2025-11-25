package org.confng.playground.envloading;

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates the new automatic environment loading feature in ConfNG.
 * 
 * <p>This test showcases:</p>
 * <ul>
 *   <li>Automatic environment detection via autoLoadConfig()</li>
 *   <li>Global configuration loading (global.properties)</li>
 *   <li>Environment-specific configuration (uat.properties, prod.properties, etc.)</li>
 *   <li>Configuration override patterns (global → environment-specific)</li>
 * </ul>
 */
public class EnvironmentAutoLoadingTest {

    /**
     * Configuration keys for the application.
     */
    public enum AppConfig implements ConfNGKey {
        // Application settings
        APP_NAME("app.name", "default-app"),
        APP_VERSION("app.version", "1.0.0"),
        APP_ENVIRONMENT("app.environment", "local"),
        APP_TIMEOUT("app.timeout", "30000"),
        APP_RETRY_COUNT("app.retry.count", "3"),
        
        // API settings
        API_URL("api.url", "http://localhost:8080"),
        API_TIMEOUT("api.timeout", "10000"),
        API_RETRY_ATTEMPTS("api.retryAttempts", "3"),
        API_RATE_LIMIT("api.rateLimit", "100"),
        
        // Database settings
        DATABASE_URL("database.url", "jdbc:h2:mem:testdb"),
        DATABASE_USERNAME("database.username", "sa"),
        DATABASE_POOL_MIN_SIZE("database.pool.minSize", "5"),
        DATABASE_POOL_MAX_SIZE("database.pool.maxSize", "20"),
        DATABASE_POOL_TIMEOUT("database.pool.timeout", "30000"),
        
        // Logging settings
        LOG_LEVEL("log.level", "INFO"),
        LOG_FORMAT("log.format", "json"),
        
        // Cache settings
        CACHE_ENABLED("cache.enabled", "true"),
        CACHE_TTL("cache.ttl", "300"),
        CACHE_MAX_SIZE("cache.maxSize", "1000"),
        
        // Feature flags
        FEATURES_NEW_UI("features.newUI", "false"),
        FEATURES_ANALYTICS("features.analytics", "false"),
        FEATURES_CACHING("features.caching", "true"),
        FEATURES_COMPRESSION("features.compression", "true");

        private final String key;
        private final String defaultValue;

        AppConfig(String key, String defaultValue) {
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

        // Just print the detected environment for visibility
        String detectedEnv = ConfNG.get(AppConfig.APP_ENVIRONMENT);
        if (detectedEnv == null || detectedEnv.isEmpty()) {
            detectedEnv = "local"; // Default
        }

        System.out.println("========================================");
        System.out.println("Detected Environment: " + detectedEnv);
        System.out.println("========================================");
    }

    @Test
    public void testGlobalConfigurationIsLoaded() {
        // These values come from global.properties
        assertThat(ConfNG.get(AppConfig.APP_NAME))
            .isEqualTo("ConfNG Environment Auto-Loading Example");
        
        assertThat(ConfNG.get(AppConfig.APP_VERSION))
            .isEqualTo("1.0.0");
        
        assertThat(ConfNG.getInt(AppConfig.APP_RETRY_COUNT))
            .isEqualTo(3);
        
        System.out.println("✅ Global configuration loaded successfully");
    }

    @Test
    public void testEnvironmentSpecificConfigurationOverridesGlobal() {
        // The environment-specific file (uat.properties or prod.properties)
        // overrides values from global.properties
        
        String environment = ConfNG.get(AppConfig.APP_ENVIRONMENT);
        System.out.println("Current environment: " + environment);
        
        if ("uat".equals(environment)) {
            // UAT-specific overrides
            assertThat(ConfNG.get(AppConfig.API_URL))
                .isEqualTo("https://uat-api.example.com");
            
            assertThat(ConfNG.getInt(AppConfig.API_TIMEOUT))
                .isEqualTo(60000); // Overridden from 10000 in global
            
            assertThat(ConfNG.get(AppConfig.LOG_LEVEL))
                .isEqualTo("DEBUG"); // Overridden from INFO in global
            
            System.out.println("✅ UAT environment configuration loaded and overrides global");
            
        } else if ("production".equals(environment) || "prod".equals(environment)) {
            // Production-specific overrides
            assertThat(ConfNG.get(AppConfig.API_URL))
                .isEqualTo("https://api.example.com");
            
            assertThat(ConfNG.getInt(AppConfig.DATABASE_POOL_MAX_SIZE))
                .isEqualTo(50); // Overridden from 20 in global
            
            assertThat(ConfNG.get(AppConfig.LOG_LEVEL))
                .isEqualTo("WARN"); // Overridden from INFO in global
            
            System.out.println("✅ Production environment configuration loaded and overrides global");
            
        } else if ("local".equals(environment)) {
            // Local environment (from local.json)
            assertThat(ConfNG.get(AppConfig.API_URL))
                .isEqualTo("http://localhost:8080");
            
            assertThat(ConfNG.get(AppConfig.DATABASE_URL))
                .isEqualTo("jdbc:h2:mem:testdb");
            
            System.out.println("✅ Local environment configuration loaded");
        }
    }

    @Test
    public void testGlobalValuesInheritedWhenNotOverridden() {
        // Values that are NOT overridden in environment-specific files
        // should still come from global.properties
        
        // These are defined in global.properties but not overridden in env files
        assertThat(ConfNG.getBoolean(AppConfig.CACHE_ENABLED))
            .isTrue();
        
        assertThat(ConfNG.getInt(AppConfig.CACHE_TTL))
            .isEqualTo(300);
        
        assertThat(ConfNG.getInt(AppConfig.CACHE_MAX_SIZE))
            .isEqualTo(1000);
        
        System.out.println("✅ Global values inherited when not overridden");
    }

    @Test
    public void testFeatureFlagsConfiguration() {
        String environment = ConfNG.get(AppConfig.APP_ENVIRONMENT);
        
        if ("uat".equals(environment)) {
            // UAT has more features enabled for testing
            assertThat(ConfNG.getBoolean(AppConfig.FEATURES_NEW_UI))
                .isTrue();
            
            assertThat(ConfNG.getBoolean(AppConfig.FEATURES_ANALYTICS))
                .isTrue();
            
            System.out.println("✅ UAT feature flags: newUI=true, analytics=true");
            
        } else if ("production".equals(environment) || "prod".equals(environment)) {
            // Production has all features enabled
            assertThat(ConfNG.getBoolean(AppConfig.FEATURES_NEW_UI))
                .isTrue();
            
            assertThat(ConfNG.getBoolean(AppConfig.FEATURES_ANALYTICS))
                .isTrue();
            
            System.out.println("✅ Production feature flags: all enabled");
            
        } else if ("local".equals(environment)) {
            // Local has minimal features
            assertThat(ConfNG.getBoolean(AppConfig.FEATURES_NEW_UI))
                .isFalse();
            
            assertThat(ConfNG.getBoolean(AppConfig.FEATURES_ANALYTICS))
                .isFalse();
            
            System.out.println("✅ Local feature flags: minimal features");
        }
    }

    @Test
    public void testDatabaseConfiguration() {
        String environment = ConfNG.get(AppConfig.APP_ENVIRONMENT);
        
        // Database pool settings should be environment-specific
        if ("uat".equals(environment)) {
            assertThat(ConfNG.getInt(AppConfig.DATABASE_POOL_MAX_SIZE))
                .isEqualTo(30);
            
        } else if ("production".equals(environment) || "prod".equals(environment)) {
            assertThat(ConfNG.getInt(AppConfig.DATABASE_POOL_MAX_SIZE))
                .isEqualTo(50);
            
            assertThat(ConfNG.getInt(AppConfig.DATABASE_POOL_MIN_SIZE))
                .isEqualTo(10);
        }
        
        // Pool timeout should come from global (not overridden)
        assertThat(ConfNG.getInt(AppConfig.DATABASE_POOL_TIMEOUT))
            .isEqualTo(30000);
        
        System.out.println("✅ Database configuration loaded correctly");
    }
}

