package org.confng.playground.propertiesconfig;

import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class demonstrating ConfNG with Properties file configuration.
 * 
 * <p>This test class showcases:</p>
 * <ul>
 *   <li>Loading multiple properties files</li>
 *   <li>Properties file precedence</li>
 *   <li>Type conversion from properties</li>
 *   <li>Sensitive data masking</li>
 *   <li>Default value handling</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 */
public class PropertiesConfigTest {
    
    @BeforeClass
    public void setup() {
        // Clear any existing sources and use defaults
        ConfNG.clearSourcesAndUseDefaults();
        
        // Load properties files in order (last loaded has higher precedence)
        ConfNG.loadProperties("src/test/resources/application.properties");
        ConfNG.loadProperties("src/test/resources/database.properties");
        ConfNG.loadProperties("src/test/resources/secrets.properties");
    }
    
    @Test(groups = "properties-config")
    public void testApplicationConfiguration() {
        // Test basic application configuration
        assertThat(ConfNG.get(PropertiesConfig.APP_NAME))
            .isEqualTo("ConfNG Properties Example");
        
        assertThat(ConfNG.get(PropertiesConfig.APP_VERSION))
            .isEqualTo("1.0.0");
        
        assertThat(ConfNG.getBoolean(PropertiesConfig.APP_DEBUG))
            .isTrue();
        
        assertThat(ConfNG.get(PropertiesConfig.APP_PROFILE))
            .isEqualTo("test");
    }
    
    @Test(groups = "properties-config")
    public void testDatabaseConfiguration() {
        // Test database configuration
        assertThat(ConfNG.get(PropertiesConfig.DATABASE_URL))
            .isEqualTo("jdbc:h2:mem:testdb");
        
        assertThat(ConfNG.get(PropertiesConfig.DATABASE_USERNAME))
            .isEqualTo("sa");
        
        assertThat(ConfNG.get(PropertiesConfig.DATABASE_DRIVER))
            .isEqualTo("org.h2.Driver");
        
        // Test integer conversion
        assertThat(ConfNG.getInt(PropertiesConfig.DATABASE_POOL_MIN_SIZE))
            .isEqualTo(5);
        
        assertThat(ConfNG.getInt(PropertiesConfig.DATABASE_POOL_MAX_SIZE))
            .isEqualTo(20);
        
        assertThat(ConfNG.getInt(PropertiesConfig.DATABASE_CONNECTION_TIMEOUT))
            .isEqualTo(30000);
        
        // Test boolean conversion
        assertThat(ConfNG.getBoolean(PropertiesConfig.DATABASE_SSL_ENABLED))
            .isFalse();
    }
    
    @Test(groups = "properties-config")
    public void testWebDriverConfiguration() {
        // Test WebDriver configuration
        assertThat(ConfNG.get(PropertiesConfig.WEBDRIVER_BROWSER))
            .isEqualTo("chrome");
        
        assertThat(ConfNG.getBoolean(PropertiesConfig.WEBDRIVER_HEADLESS))
            .isFalse();
        
        assertThat(ConfNG.getInt(PropertiesConfig.WEBDRIVER_TIMEOUT))
            .isEqualTo(30);
        
        assertThat(ConfNG.getInt(PropertiesConfig.WEBDRIVER_IMPLICIT_WAIT))
            .isEqualTo(10);
        
        assertThat(ConfNG.getInt(PropertiesConfig.WEBDRIVER_PAGE_LOAD_TIMEOUT))
            .isEqualTo(60);
    }
    
    @Test(groups = "properties-config")
    public void testFeatureFlags() {
        // Test feature flags
        assertThat(ConfNG.getBoolean(PropertiesConfig.FEATURES_NEW_UI))
            .isTrue();
        
        assertThat(ConfNG.getBoolean(PropertiesConfig.FEATURES_BETA_API))
            .isFalse();
        
        assertThat(ConfNG.getBoolean(PropertiesConfig.FEATURES_ANALYTICS))
            .isTrue();
        
        assertThat(ConfNG.getBoolean(PropertiesConfig.FEATURES_CACHING))
            .isTrue();
    }
    
    @Test(groups = "properties-config")
    public void testPerformanceSettings() {
        // Test performance configuration
        assertThat(ConfNG.getInt(PropertiesConfig.CACHE_TTL))
            .isEqualTo(3600);
        
        assertThat(ConfNG.getInt(PropertiesConfig.CACHE_MAX_SIZE))
            .isEqualTo(1000);
        
        assertThat(ConfNG.getInt(PropertiesConfig.THREAD_POOL_SIZE))
            .isEqualTo(10);
        
        assertThat(ConfNG.getInt(PropertiesConfig.BATCH_SIZE))
            .isEqualTo(100);
    }
    
    @Test(groups = "properties-config")
    public void testSensitiveDataMasking() {
        // Test that sensitive values are masked for display
        assertThat(ConfNG.getForDisplay(PropertiesConfig.API_STRIPE_SECRET_KEY))
            .isEqualTo("***MASKED***");
        
        assertThat(ConfNG.getForDisplay(PropertiesConfig.API_SENDGRID_API_KEY))
            .isEqualTo("***MASKED***");
        
        assertThat(ConfNG.getForDisplay(PropertiesConfig.OAUTH_CLIENT_SECRET))
            .isEqualTo("***MASKED***");
        
        assertThat(ConfNG.getForDisplay(PropertiesConfig.DATABASE_ADMIN_PASSWORD))
            .isEqualTo("***MASKED***");
        
        // Test that actual values are still accessible (not masked)
        assertThat(ConfNG.get(PropertiesConfig.API_STRIPE_SECRET_KEY))
            .isEqualTo("sk_test_123456789abcdef");
        
        assertThat(ConfNG.get(PropertiesConfig.DATABASE_ADMIN_USERNAME))
            .isEqualTo("admin");
    }
    
    @Test(groups = "properties-config")
    public void testDefaultValues() {
        // Test keys that don't exist in properties files fall back to defaults
        assertThat(ConfNG.get(PropertiesConfig.APP_DESCRIPTION))
            .isEqualTo("Example application demonstrating ConfNG properties configuration");
        
        // Test that missing sensitive keys return null (no default provided)
        assertThat(ConfNG.get(PropertiesConfig.ENCRYPTION_MASTER_KEY))
            .isEqualTo("master-encryption-key-123");
    }
    
    @Test(groups = "properties-config")
    public void testPropertiesFilePrecedence() {
        // Test that later loaded properties files override earlier ones
        // database.properties should override application.properties for database.driver
        assertThat(ConfNG.get(PropertiesConfig.DATABASE_DRIVER))
            .isEqualTo("org.h2.Driver");
        
        // secrets.properties should provide sensitive values
        assertThat(ConfNG.get(PropertiesConfig.JWT_SIGNING_KEY))
            .isEqualTo("jwt-secret-key-123456789");
    }
    
    @Test(groups = "properties-config")
    public void testAllConfigurationDisplay() {
        // Test displaying all configuration (sensitive values should be masked)
        String allConfig = ConfNG.getAllForDisplay(
            PropertiesConfig.APP_NAME,
            PropertiesConfig.DATABASE_URL,
            PropertiesConfig.API_STRIPE_SECRET_KEY,
            PropertiesConfig.WEBDRIVER_BROWSER
        );
        
        assertThat(allConfig)
            .contains("app.name = ConfNG Properties Example")
            .contains("database.url = jdbc:h2:mem:testdb")
            .contains("api.stripe.secret-key = ***MASKED***")
            .contains("webdriver.browser = chrome")
            .contains("(sensitive)");
    }
}
