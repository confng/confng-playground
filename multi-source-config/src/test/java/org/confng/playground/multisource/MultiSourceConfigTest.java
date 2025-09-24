package org.confng.playground.multisource;

import org.confng.ConfNG;
import org.confng.sources.ConfigSource;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class demonstrating ConfNG with multiple configuration sources.
 * 
 * <p>This test class showcases:</p>
 * <ul>
 *   <li>Multiple configuration sources working together</li>
 *   <li>Source precedence and priority ordering</li>
 *   <li>Configuration override patterns</li>
 *   <li>Dynamic source management</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 */
public class MultiSourceConfigTest {
    
    @BeforeClass
    public void setupMultipleSources() {
        // Clear any existing sources and use defaults (Env + System Properties)
        ConfNG.clearSourcesAndUseDefaults();
        
        // Add JSON source (priority 30)
        ConfNG.loadJson("src/test/resources/config.json");
        
        // Add Properties source (priority 25)
        ConfNG.loadProperties("src/test/resources/application.properties");
    }
    
    @Test(groups = "multi-source")
    public void testSourcePrecedenceWithSystemProperties() {
        // System properties (priority 50) should override JSON (priority 30)
        // We set app.environment=test in build.gradle systemProperty
        assertThat(ConfNG.get(MultiSourceConfig.APP_ENVIRONMENT))
            .isEqualTo("test"); // From system property, not "development" from JSON
        
        // test.precedence is set in system properties
        assertThat(ConfNG.get(MultiSourceConfig.TEST_PRECEDENCE))
            .isEqualTo("system-property");
    }
    
    @Test(groups = "multi-source")
    public void testJsonOverridesProperties() {
        // JSON (priority 30) should override Properties (priority 25)
        assertThat(ConfNG.get(MultiSourceConfig.APP_NAME))
            .isEqualTo("Multi-Source ConfNG Example"); // From JSON, not properties
        
        assertThat(ConfNG.get(MultiSourceConfig.APP_VERSION))
            .isEqualTo("2.0.0"); // From JSON, not "1.0.0" from properties
        
        assertThat(ConfNG.getInt(MultiSourceConfig.DATABASE_POOL_MAX_SIZE))
            .isEqualTo(15); // From JSON, not 10 from properties
    }
    
    @Test(groups = "multi-source")
    public void testPropertiesFallback() {
        // Properties should provide values not in JSON
        assertThat(ConfNG.get(MultiSourceConfig.LOGGING_LEVEL))
            .isEqualTo("INFO"); // Only in properties file
        
        assertThat(ConfNG.get(MultiSourceConfig.LOGGING_FILE))
            .isEqualTo("application.log"); // Only in properties file
        
        assertThat(ConfNG.getInt(MultiSourceConfig.CACHE_SIZE))
            .isEqualTo(1000); // Only in properties file
    }
    
    @Test(groups = "multi-source")
    public void testDefaultValueFallback() {
        // Keys not in any source should use default values
        assertThat(ConfNG.get(MultiSourceConfig.CUSTOM_VALUE))
            .isEqualTo("default-custom"); // Not in any file, uses default
    }
    
    @Test(groups = "multi-source")
    public void testFeatureFlagConfiguration() {
        // Test feature flags from different sources
        assertThat(ConfNG.getBoolean(MultiSourceConfig.FEATURES_NEW_UI))
            .isTrue(); // From JSON (true), overrides properties (false)
        
        assertThat(ConfNG.getBoolean(MultiSourceConfig.FEATURES_ANALYTICS))
            .isFalse(); // From JSON (false), same as properties
        
        assertThat(ConfNG.getBoolean(MultiSourceConfig.FEATURES_CACHING))
            .isTrue(); // From JSON (true), same as properties
        
        assertThat(ConfNG.getBoolean(MultiSourceConfig.FEATURES_MONITORING))
            .isTrue(); // From JSON (true), overrides properties (false)
    }
    
    @Test(groups = "multi-source")
    public void testDatabaseConfiguration() {
        // Test database configuration from multiple sources
        assertThat(ConfNG.get(MultiSourceConfig.DATABASE_URL))
            .isEqualTo("jdbc:postgresql://localhost:5432/devdb"); // From JSON
        
        assertThat(ConfNG.get(MultiSourceConfig.DATABASE_USERNAME))
            .isEqualTo("dev_user"); // From JSON
        
        // Pool configuration
        assertThat(ConfNG.getInt(MultiSourceConfig.DATABASE_POOL_MAX_SIZE))
            .isEqualTo(15); // From JSON
        
        assertThat(ConfNG.getInt(MultiSourceConfig.DATABASE_POOL_MIN_SIZE))
            .isEqualTo(3); // From JSON
        
        // Timeout might come from system property if set
        Integer poolTimeout = ConfNG.getInt(MultiSourceConfig.DATABASE_POOL_TIMEOUT);
        // Could be 45000 (system property) or 30000 (JSON) depending on test execution
        assertThat(poolTimeout).isIn(30000, 45000);
    }
    
    @Test(groups = "multi-source")
    public void testWebDriverConfiguration() {
        // Test WebDriver configuration precedence
        assertThat(ConfNG.get(MultiSourceConfig.WEBDRIVER_BROWSER))
            .isEqualTo("firefox"); // From JSON, overrides "chrome" from properties
        
        assertThat(ConfNG.getBoolean(MultiSourceConfig.WEBDRIVER_HEADLESS))
            .isTrue(); // From JSON, overrides false from properties
        
        assertThat(ConfNG.getInt(MultiSourceConfig.WEBDRIVER_TIMEOUT))
            .isEqualTo(45); // From JSON, overrides 30 from properties
    }
    
    @Test(groups = "multi-source")
    public void testApiConfiguration() {
        // Test API configuration
        assertThat(ConfNG.get(MultiSourceConfig.API_BASE_URL))
            .isEqualTo("https://api.example.com"); // From JSON
        
        assertThat(ConfNG.getInt(MultiSourceConfig.API_TIMEOUT))
            .isEqualTo(15000); // From JSON
        
        assertThat(ConfNG.getInt(MultiSourceConfig.API_RETRY_ATTEMPTS))
            .isEqualTo(5); // From JSON, overrides 3 from properties
    }
    
    @Test(groups = "multi-source")
    public void testDynamicSourceAddition() {
        // Create a custom high-priority source
        CustomConfigSource customSource = new CustomConfigSource();
        ConfNG.addSource(customSource);
        
        // Verify the new source takes precedence
        assertThat(ConfNG.get(MultiSourceConfig.CUSTOM_VALUE))
            .isEqualTo("from-custom-source");
        
        // Verify it doesn't affect other values
        assertThat(ConfNG.get(MultiSourceConfig.APP_NAME))
            .isEqualTo("Multi-Source ConfNG Example");
    }
    
    @Test(groups = "multi-source")
    public void testConfigurationDisplay() {
        // Test displaying configuration from multiple sources
        String allConfig = ConfNG.getAllForDisplay(
            MultiSourceConfig.APP_NAME,
            MultiSourceConfig.DATABASE_URL,
            MultiSourceConfig.FEATURES_NEW_UI,
            MultiSourceConfig.DATABASE_PASSWORD
        );
        
        assertThat(allConfig)
            .contains("app.name = Multi-Source ConfNG Example")
            .contains("database.url = jdbc:postgresql://localhost:5432/devdb")
            .contains("features.newUI = true")
            .contains("database.password = ***MASKED***")
            .contains("(sensitive)");
    }
    
    /**
     * Custom configuration source for testing dynamic source addition.
     */
    private static class CustomConfigSource implements ConfigSource {
        
        @Override
        public String getName() {
            return "CustomTestSource";
        }
        
        @Override
        public Optional<String> get(String key) {
            if ("custom.value".equals(key)) {
                return Optional.of("from-custom-source");
            }
            return Optional.empty();
        }
        
        @Override
        public int getPriority() {
            return 90; // High priority to override other sources
        }
    }
}