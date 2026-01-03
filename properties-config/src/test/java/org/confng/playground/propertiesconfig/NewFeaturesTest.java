package org.confng.playground.propertiesconfig;

import org.confng.ConfNG;
import org.confng.ConfigSourceInfo;
import org.confng.ValidationResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test class demonstrating ConfNG 1.0.3 new features.
 * 
 * <p>This test class showcases the 5 new features in version 1.0.3:</p>
 * <ul>
 *   <li>Typed Configuration Getters (getLong, getDouble, getList, getDuration)</li>
 *   <li>Optional/Required Configuration (getOptional, getRequired, getOrDefault)</li>
 *   <li>Configuration Validation Framework (@Required, @NotEmpty, @Pattern, @Range)</li>
 *   <li>Configuration Source Diagnostics (getSourceInfo, getAllSourceInfo)</li>
 *   <li>Prefix-based Configuration Retrieval (getByPrefix, getKeysWithPrefix)</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @since 1.0.3
 */
public class NewFeaturesTest {
    
    @BeforeClass
    public void setup() {
        ConfNG.clearSourcesAndUseDefaults();
        ConfNG.loadProperties("src/test/resources/application.properties");
        ConfNG.loadProperties("src/test/resources/database.properties");
    }
    
    // ========== Feature 1: Typed Configuration Getters ==========
    
    @Test(groups = "new-features-1.0.3")
    public void testGetLong() {
        // Test getLong for large numeric values
        Long connectionTimeout = ConfNG.getLong(PropertiesConfig.DATABASE_CONNECTION_TIMEOUT);
        assertThat(connectionTimeout).isEqualTo(30000L);
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testGetDouble() {
        // Test getDouble for decimal values
        Double cacheSize = ConfNG.getDouble(PropertiesConfig.CACHE_MAX_SIZE);
        assertThat(cacheSize).isEqualTo(1000.0);
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testGetList() {
        // Test getList for comma-separated values
        // Note: This requires a list-type config value in properties
        List<String> browsers = ConfNG.getList(PropertiesConfig.WEBDRIVER_BROWSER);
        assertThat(browsers).isNotEmpty();
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testGetDuration() {
        // Test getDuration for time-based values
        Duration timeout = ConfNG.getDuration(PropertiesConfig.CACHE_TTL);
        assertThat(timeout).isNotNull();
    }
    
    // ========== Feature 2: Optional/Required Configuration ==========
    
    @Test(groups = "new-features-1.0.3")
    public void testGetOptional() {
        // Test getOptional returns Optional with value
        Optional<String> appName = ConfNG.getOptional(PropertiesConfig.APP_NAME);
        assertThat(appName).isPresent();
        assertThat(appName.get()).isEqualTo("ConfNG Properties Example");
        
        // Test getOptional with missing value returns empty Optional
        Optional<String> missing = ConfNG.getOptional(PropertiesConfig.DATABASE_SSL_TRUST_STORE_PATH);
        assertThat(missing).isEmpty();
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testGetRequired() {
        // Test getRequired returns value when present
        String appName = ConfNG.getRequired(PropertiesConfig.APP_NAME);
        assertThat(appName).isEqualTo("ConfNG Properties Example");
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testGetOrDefault() {
        // Test getOrDefault with existing value
        String browser = ConfNG.getOrDefault(PropertiesConfig.WEBDRIVER_BROWSER, "firefox");
        assertThat(browser).isEqualTo("chrome");
        
        // Test getOrDefault with missing value uses fallback
        String missingValue = ConfNG.getOrDefault(PropertiesConfig.DATABASE_SSL_TRUST_STORE_PATH, "/default/path");
        assertThat(missingValue).isEqualTo("/default/path");
    }
    
    // ========== Feature 3: Configuration Validation ==========
    
    @Test(groups = "new-features-1.0.3")
    public void testValidation() {
        // Test validation of configuration values
        ValidationResult result = ConfNG.validate(
            PropertiesConfig.APP_NAME,
            PropertiesConfig.DATABASE_URL,
            PropertiesConfig.WEBDRIVER_BROWSER
        );
        
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrorCount()).isEqualTo(0);
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testValidationWithAllKeys() {
        // Validate all configuration keys
        ValidationResult result = ConfNG.validate(PropertiesConfig.values());
        
        // Log any validation errors for debugging
        if (!result.isValid()) {
            result.getErrors().forEach(error -> 
                System.out.println("Validation error: " + error.getKey() + " - " + error.getMessage())
            );
        }
    }
    
    // ========== Feature 4: Source Diagnostics ==========
    
    @Test(groups = "new-features-1.0.3")
    public void testGetSourceInfo() {
        // Test getting source info for a single key
        ConfigSourceInfo info = ConfNG.getSourceInfo(PropertiesConfig.APP_NAME);
        
        assertThat(info).isNotNull();
        assertThat(info.getKey()).isEqualTo("app.name");
        assertThat(info.isFound()).isTrue();
        assertThat(info.getSourceName()).isNotEmpty();
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testGetAllSourceInfo() {
        // Test getting source info for multiple keys
        Map<String, ConfigSourceInfo> infos = ConfNG.getAllSourceInfo(
            PropertiesConfig.APP_NAME,
            PropertiesConfig.DATABASE_URL,
            PropertiesConfig.WEBDRIVER_BROWSER
        );
        
        assertThat(infos).hasSize(3);
        assertThat(infos.get("app.name")).isNotNull();
        assertThat(infos.get("database.url")).isNotNull();
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testSourceInfoForSensitiveData() {
        // Test that sensitive values are masked in source info
        ConfigSourceInfo info = ConfNG.getSourceInfo(PropertiesConfig.DATABASE_ADMIN_PASSWORD);
        
        if (info.isFound() && info.isSensitive()) {
            assertThat(info.getValue()).isEqualTo("***MASKED***");
        }
    }
    
    // ========== Feature 5: Prefix-based Retrieval ==========
    
    @Test(groups = "new-features-1.0.3")
    public void testGetByPrefix() {
        // Test getting all values with a prefix
        Map<String, String> dbConfig = ConfNG.getByPrefix("database.");
        
        assertThat(dbConfig).isNotEmpty();
        assertThat(dbConfig.keySet()).anyMatch(key -> key.startsWith("database."));
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testGetKeysWithPrefix() {
        // Test getting all keys with a prefix
        Set<String> featureKeys = ConfNG.getKeysWithPrefix("features.");
        
        assertThat(featureKeys).isNotEmpty();
        assertThat(featureKeys).allMatch(key -> key.startsWith("features."));
    }
    
    @Test(groups = "new-features-1.0.3")
    public void testPrefixRetrievalForFeatureFlags() {
        // Practical example: loading all feature flags
        Map<String, String> features = ConfNG.getByPrefix("features.");
        
        features.forEach((key, value) -> {
            String featureName = key.substring("features.".length());
            boolean enabled = Boolean.parseBoolean(value);
            System.out.println("Feature '" + featureName + "' is " + (enabled ? "enabled" : "disabled"));
        });
    }
}

