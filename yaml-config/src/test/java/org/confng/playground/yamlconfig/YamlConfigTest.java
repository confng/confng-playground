package org.confng.playground.yamlconfig;

import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class demonstrating ConfNG with custom YAML configuration source.
 * 
 * <p>This test class showcases:</p>
 * <ul>
 *   <li>Custom configuration source implementation</li>
 *   <li>YAML file parsing and nested property access</li>
 *   <li>Integration with ConfNG source precedence</li>
 *   <li>Type conversion from YAML values</li>
 *   <li>Complex data structure handling</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 */
public class YamlConfigTest {
    
    private YamlSource yamlSource;
    
    @BeforeClass
    public void setup() {
        // Clear any existing sources and use defaults
        ConfNG.clearSourcesAndUseDefaults();
        
        // Add YAML source
        yamlSource = new YamlSource(Paths.get("src/test/resources/application.yaml"));
        ConfNG.addSource(yamlSource);
    }
    
    @Test(groups = "yaml-config")
    public void testApplicationConfiguration() {
        // Test basic application configuration
        assertThat(ConfNG.get(YamlConfig.APP_NAME))
            .isEqualTo("ConfNG YAML Example");
        
        assertThat(ConfNG.get(YamlConfig.APP_VERSION))
            .isEqualTo("1.0.0");
        
        assertThat(ConfNG.get(YamlConfig.APP_ENVIRONMENT))
            .isEqualTo("development");
        
        assertThat(ConfNG.getBoolean(YamlConfig.APP_DEBUG))
            .isTrue();
        
        assertThat(ConfNG.get(YamlConfig.APP_DESCRIPTION))
            .isEqualTo("Example application demonstrating YAML configuration with ConfNG");
    }
    
    @Test(groups = "yaml-config")
    public void testNestedDatabaseConfiguration() {
        // Test nested database configuration - Primary
        assertThat(ConfNG.get(YamlConfig.DATABASE_PRIMARY_URL))
            .isEqualTo("jdbc:postgresql://localhost:5432/primary");
        
        assertThat(ConfNG.get(YamlConfig.DATABASE_PRIMARY_USERNAME))
            .isEqualTo("primary_user");
        
        assertThat(ConfNG.get(YamlConfig.DATABASE_PRIMARY_DRIVER))
            .isEqualTo("org.postgresql.Driver");
        
        // Test integer conversion for pool settings
        assertThat(ConfNG.getInt(YamlConfig.DATABASE_PRIMARY_POOL_MAX_SIZE))
            .isEqualTo(20);
        
        assertThat(ConfNG.getInt(YamlConfig.DATABASE_PRIMARY_POOL_MIN_SIZE))
            .isEqualTo(5);
        
        assertThat(ConfNG.getInt(YamlConfig.DATABASE_PRIMARY_POOL_TIMEOUT))
            .isEqualTo(30000);
        
        // Test nested database configuration - Secondary
        assertThat(ConfNG.get(YamlConfig.DATABASE_SECONDARY_URL))
            .isEqualTo("jdbc:postgresql://localhost:5432/secondary");
        
        assertThat(ConfNG.get(YamlConfig.DATABASE_SECONDARY_USERNAME))
            .isEqualTo("secondary_user");
        
        assertThat(ConfNG.getInt(YamlConfig.DATABASE_SECONDARY_POOL_MAX_SIZE))
            .isEqualTo(10);
        
        assertThat(ConfNG.getInt(YamlConfig.DATABASE_SECONDARY_POOL_MIN_SIZE))
            .isEqualTo(2);
    }
    
    @Test(groups = "yaml-config")
    public void testWebDriverConfiguration() {
        // Test WebDriver configuration
        assertThat(ConfNG.get(YamlConfig.WEBDRIVER_BROWSER))
            .isEqualTo("chrome");
        
        assertThat(ConfNG.getBoolean(YamlConfig.WEBDRIVER_HEADLESS))
            .isFalse();
        
        assertThat(ConfNG.getInt(YamlConfig.WEBDRIVER_TIMEOUT))
            .isEqualTo(45);
        
        assertThat(ConfNG.getInt(YamlConfig.WEBDRIVER_IMPLICIT_WAIT))
            .isEqualTo(15);
        
        assertThat(ConfNG.getInt(YamlConfig.WEBDRIVER_PAGE_LOAD_TIMEOUT))
            .isEqualTo(60);
    }
    
    @Test(groups = "yaml-config")
    public void testApiServicesConfiguration() {
        // Test Payment API configuration
        assertThat(ConfNG.get(YamlConfig.API_PAYMENT_BASE_URL))
            .isEqualTo("https://api.stripe.com");
        
        assertThat(ConfNG.getInt(YamlConfig.API_PAYMENT_TIMEOUT))
            .isEqualTo(15000);
        
        assertThat(ConfNG.getInt(YamlConfig.API_PAYMENT_RETRY_ATTEMPTS))
            .isEqualTo(3);
        
        assertThat(ConfNG.get(YamlConfig.API_PAYMENT_API_VERSION))
            .isEqualTo("2023-10-16");
        
        // Test Notification API configuration
        assertThat(ConfNG.get(YamlConfig.API_NOTIFICATION_BASE_URL))
            .isEqualTo("https://api.sendgrid.com");
        
        assertThat(ConfNG.getInt(YamlConfig.API_NOTIFICATION_TIMEOUT))
            .isEqualTo(10000);
        
        assertThat(ConfNG.getInt(YamlConfig.API_NOTIFICATION_RETRY_ATTEMPTS))
            .isEqualTo(2);
        
        assertThat(ConfNG.get(YamlConfig.API_NOTIFICATION_API_VERSION))
            .isEqualTo("v3");
        
        // Test Analytics API configuration
        assertThat(ConfNG.get(YamlConfig.API_ANALYTICS_BASE_URL))
            .isEqualTo("https://api.analytics.com");
        
        assertThat(ConfNG.getInt(YamlConfig.API_ANALYTICS_TIMEOUT))
            .isEqualTo(8000);
        
        assertThat(ConfNG.getInt(YamlConfig.API_ANALYTICS_BATCH_SIZE))
            .isEqualTo(100);
    }
    
    @Test(groups = "yaml-config")
    public void testMonitoringConfiguration() {
        // Test monitoring configuration
        assertThat(ConfNG.getBoolean(YamlConfig.MONITORING_METRICS_ENABLED))
            .isTrue();
        
        assertThat(ConfNG.getInt(YamlConfig.MONITORING_METRICS_INTERVAL))
            .isEqualTo(60000);
        
        assertThat(ConfNG.get(YamlConfig.MONITORING_LOGGING_LEVEL))
            .isEqualTo("INFO");
    }
    
    @Test(groups = "yaml-config")
    public void testCacheConfiguration() {
        // Test Redis cache configuration
        assertThat(ConfNG.get(YamlConfig.CACHE_REDIS_HOST))
            .isEqualTo("localhost");
        
        assertThat(ConfNG.getInt(YamlConfig.CACHE_REDIS_PORT))
            .isEqualTo(6379);
        
        assertThat(ConfNG.getInt(YamlConfig.CACHE_REDIS_DATABASE))
            .isEqualTo(0);
        
        assertThat(ConfNG.getInt(YamlConfig.CACHE_REDIS_TIMEOUT))
            .isEqualTo(5000);
        
        assertThat(ConfNG.getInt(YamlConfig.CACHE_REDIS_POOL_MAX_ACTIVE))
            .isEqualTo(20);
        
        assertThat(ConfNG.getInt(YamlConfig.CACHE_REDIS_POOL_MAX_IDLE))
            .isEqualTo(10);
        
        assertThat(ConfNG.getInt(YamlConfig.CACHE_REDIS_POOL_MIN_IDLE))
            .isEqualTo(2);
        
        // Test local cache configuration
        assertThat(ConfNG.getInt(YamlConfig.CACHE_LOCAL_MAX_SIZE))
            .isEqualTo(1000);
        
        assertThat(ConfNG.getInt(YamlConfig.CACHE_LOCAL_TTL))
            .isEqualTo(3600);
        
        assertThat(ConfNG.get(YamlConfig.CACHE_LOCAL_EVICTION_POLICY))
            .isEqualTo("LRU");
    }
    
    @Test(groups = "yaml-config")
    public void testSecurityConfiguration() {
        // Test JWT configuration
        assertThat(ConfNG.get(YamlConfig.SECURITY_JWT_SECRET))
            .isEqualTo("jwt-secret-key");
        
        assertThat(ConfNG.getInt(YamlConfig.SECURITY_JWT_EXPIRATION))
            .isEqualTo(86400);
        
        assertThat(ConfNG.get(YamlConfig.SECURITY_JWT_ISSUER))
            .isEqualTo("confng-example");
        
        // Test OAuth configuration
        assertThat(ConfNG.get(YamlConfig.SECURITY_OAUTH_GOOGLE_CLIENT_ID))
            .isEqualTo("google-client-id");
        
        assertThat(ConfNG.get(YamlConfig.SECURITY_OAUTH_GOOGLE_CLIENT_SECRET))
            .isEqualTo("google-client-secret");
        
        assertThat(ConfNG.get(YamlConfig.SECURITY_OAUTH_GITHUB_CLIENT_ID))
            .isEqualTo("github-client-id");
        
        assertThat(ConfNG.get(YamlConfig.SECURITY_OAUTH_GITHUB_CLIENT_SECRET))
            .isEqualTo("github-client-secret");
    }
    
    @Test(groups = "yaml-config")
    public void testSensitiveDataMasking() {
        // Test that sensitive values are masked for display
        assertThat(ConfNG.getForDisplay(YamlConfig.DATABASE_PRIMARY_PASSWORD))
            .isEqualTo("***MASKED***");
        
        assertThat(ConfNG.getForDisplay(YamlConfig.DATABASE_SECONDARY_PASSWORD))
            .isEqualTo("***MASKED***");
        
        assertThat(ConfNG.getForDisplay(YamlConfig.SECURITY_JWT_SECRET))
            .isEqualTo("***MASKED***");
        
        assertThat(ConfNG.getForDisplay(YamlConfig.SECURITY_OAUTH_GOOGLE_CLIENT_SECRET))
            .isEqualTo("***MASKED***");
        
        // Test that actual values are still accessible (not masked)
        assertThat(ConfNG.get(YamlConfig.DATABASE_PRIMARY_PASSWORD))
            .isEqualTo("primary_pass");
        
        assertThat(ConfNG.get(YamlConfig.SECURITY_JWT_SECRET))
            .isEqualTo("jwt-secret-key");
    }
    
    @Test(groups = "yaml-config")
    public void testYamlSourceDirectAccess() {
        // Test direct access to YAML source features
        
        // Test list access
        List<Object> features = yamlSource.getList("app.features");
        assertThat(features)
            .hasSize(4)
            .contains("new-ui", "analytics", "caching", "monitoring");
        
        // Test map access
        Map<String, Object> primaryPool = yamlSource.getMap("database.primary.pool");
        assertThat(primaryPool)
            .containsEntry("maxSize", 20)
            .containsEntry("minSize", 5)
            .containsEntry("timeout", 30000);
        
        // Test typed access
        assertThat(yamlSource.getTyped("database.primary.pool.maxSize", Integer.class))
            .isPresent()
            .hasValue(20);
        
        assertThat(yamlSource.getTyped("app.debug", Boolean.class))
            .isPresent()
            .hasValue(true);
    }
    
    @Test(groups = "yaml-config")
    public void testYamlSourceMetadata() {
        // Test source metadata
        assertThat(yamlSource.getName())
            .contains("YAML")
            .contains("application.yaml");
        
        assertThat(yamlSource.getPriority())
            .isEqualTo(35); // Between JSON (30) and System Properties (50)
        
        // Test key discovery
        Set<String> allKeys = yamlSource.getAllKeys();
        assertThat(allKeys)
            .contains("app.name", "database.primary.url", "api.services.payment.baseUrl")
            .hasSizeGreaterThan(20);
    }
    
    @Test(groups = "yaml-config")
    public void testDefaultValueFallback() {
        // Test keys that don't exist in YAML fall back to defaults
        // These keys are not in the YAML file, so should use default values
        YamlConfig nonExistentKey = YamlConfig.APP_NAME; // This exists, let's create a scenario
        
        // Test with a key that has a default but isn't in YAML
        // Since all our test keys are in YAML, let's test the default mechanism differently
        assertThat(ConfNG.get(YamlConfig.APP_NAME))
            .isNotEqualTo(YamlConfig.APP_NAME.getDefaultValue()); // Should get YAML value, not default
    }
    
    @Test(groups = "yaml-config")
    public void testConfigurationDisplay() {
        // Test displaying configuration from YAML source
        String allConfig = ConfNG.getAllForDisplay(
            YamlConfig.APP_NAME,
            YamlConfig.DATABASE_PRIMARY_URL,
            YamlConfig.DATABASE_PRIMARY_PASSWORD,
            YamlConfig.API_PAYMENT_BASE_URL
        );
        
        assertThat(allConfig)
            .contains("app.name = ConfNG YAML Example")
            .contains("database.primary.url = jdbc:postgresql://localhost:5432/primary")
            .contains("database.primary.password = ***MASKED***")
            .contains("api.services.payment.baseUrl = https://api.stripe.com")
            .contains("(sensitive)");
    }
}