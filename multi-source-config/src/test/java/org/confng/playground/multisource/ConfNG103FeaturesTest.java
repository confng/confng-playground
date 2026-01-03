package org.confng.playground.multisource;

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

/**
 * Test class demonstrating ConfNG 1.0.3 new features with multi-source configuration.
 * 
 * <p>This test showcases how the new 1.0.3 features work across multiple configuration sources:</p>
 * <ul>
 *   <li>Source diagnostics showing which source provided each value</li>
 *   <li>Prefix-based retrieval across all sources</li>
 *   <li>Validation across multi-source configuration</li>
 * </ul>
 * 
 * @author Bharat Kumar Malviya
 * @since 1.0.3
 */
public class ConfNG103FeaturesTest {
    
    @BeforeClass
    public void setup() {
        ConfNG.clearSourcesAndUseDefaults();
        ConfNG.loadProperties("src/test/resources/application.properties");
        ConfNG.loadJson("src/test/resources/config.json");
    }
    
    // ========== Source Diagnostics with Multi-Source ==========
    
    @Test(groups = "multi-source-1.0.3")
    public void testSourceDiagnosticsAcrossSources() {
        // Get source info to see which source provided each value
        ConfigSourceInfo appNameInfo = ConfNG.getSourceInfo(MultiSourceConfig.APP_NAME);
        ConfigSourceInfo dbUrlInfo = ConfNG.getSourceInfo(MultiSourceConfig.DATABASE_URL);
        
        System.out.println("=== Source Diagnostics ===");
        System.out.println("app.name: " + appNameInfo.getValue() + " (from " + appNameInfo.getSourceName() + ")");
        System.out.println("database.url: " + dbUrlInfo.getValue() + " (from " + dbUrlInfo.getSourceName() + ")");
        
        assertThat(appNameInfo.isFound()).isTrue();
        assertThat(dbUrlInfo.isFound()).isTrue();
    }
    
    @Test(groups = "multi-source-1.0.3")
    public void testAllSourceInfoForDebugging() {
        // Get all source info for debugging configuration issues
        Map<String, ConfigSourceInfo> allInfo = ConfNG.getAllSourceInfo(MultiSourceConfig.values());
        
        System.out.println("=== All Configuration Sources ===");
        allInfo.forEach((key, info) -> {
            String status = info.isFound() ? "✓" : "✗";
            String source = info.isFromDefault() ? "Default" : info.getSourceName();
            System.out.printf("%s %s = %s [%s]%n", status, key, info.getValue(), source);
        });
        
        assertThat(allInfo).isNotEmpty();
    }
    
    // ========== Prefix-based Retrieval Across Sources ==========
    
    @Test(groups = "multi-source-1.0.3")
    public void testPrefixRetrievalFromMultipleSources() {
        // Get all database configuration regardless of source
        Map<String, String> dbConfig = ConfNG.getByPrefix("database.");
        
        System.out.println("=== Database Configuration (from all sources) ===");
        dbConfig.forEach((key, value) -> System.out.println(key + " = " + value));
        
        assertThat(dbConfig).isNotEmpty();
    }
    
    @Test(groups = "multi-source-1.0.3")
    public void testFeatureFlagsFromMultipleSources() {
        // Get all feature flags
        Map<String, String> features = ConfNG.getByPrefix("features.");
        
        System.out.println("=== Feature Flags ===");
        features.forEach((key, value) -> {
            String featureName = key.substring("features.".length());
            boolean enabled = Boolean.parseBoolean(value);
            System.out.println("Feature '" + featureName + "': " + (enabled ? "ENABLED" : "DISABLED"));
        });
        
        assertThat(features).isNotEmpty();
    }
    
    @Test(groups = "multi-source-1.0.3")
    public void testGetKeysWithPrefixForDiscovery() {
        // Discover all API-related configuration keys
        Set<String> apiKeys = ConfNG.getKeysWithPrefix("api.");
        
        System.out.println("=== API Configuration Keys ===");
        apiKeys.forEach(System.out::println);
        
        assertThat(apiKeys).isNotEmpty();
    }
    
    // ========== Optional/Required with Multi-Source ==========
    
    @Test(groups = "multi-source-1.0.3")
    public void testOptionalWithMultipleSources() {
        // Optional values work across all sources
        Optional<String> appEnv = ConfNG.getOptional(MultiSourceConfig.APP_ENVIRONMENT);
        
        appEnv.ifPresentOrElse(
            env -> System.out.println("Environment: " + env),
            () -> System.out.println("Environment not configured")
        );
        
        assertThat(appEnv).isPresent();
    }
    
    @Test(groups = "multi-source-1.0.3")
    public void testGetOrDefaultWithSourceFallback() {
        // getOrDefault provides explicit fallback after all sources checked
        String customValue = ConfNG.getOrDefault(MultiSourceConfig.CUSTOM_VALUE, "explicit-fallback");
        
        System.out.println("Custom value: " + customValue);
        assertThat(customValue).isNotNull();
    }
    
    // ========== Typed Getters with Multi-Source ==========
    
    @Test(groups = "multi-source-1.0.3")
    public void testTypedGettersFromDifferentSources() {
        // Typed getters work regardless of which source provides the value
        Long timeout = ConfNG.getLong(MultiSourceConfig.APP_TIMEOUT);
        Integer poolSize = ConfNG.getInt(MultiSourceConfig.DATABASE_POOL_MAX_SIZE);
        Boolean headless = ConfNG.getBoolean(MultiSourceConfig.WEBDRIVER_HEADLESS);
        
        System.out.println("App timeout: " + timeout + "ms");
        System.out.println("Pool size: " + poolSize);
        System.out.println("Headless: " + headless);
        
        assertThat(timeout).isNotNull();
        assertThat(poolSize).isNotNull();
        assertThat(headless).isNotNull();
    }
    
    // ========== Validation Across Sources ==========
    
    @Test(groups = "multi-source-1.0.3")
    public void testValidationAcrossAllSources() {
        // Validate configuration from all sources
        ValidationResult result = ConfNG.validate(MultiSourceConfig.values());
        
        System.out.println("=== Validation Result ===");
        System.out.println("Valid: " + result.isValid());
        System.out.println("Error count: " + result.getErrorCount());
        
        if (!result.isValid()) {
            result.getErrors().forEach(error -> 
                System.out.println("  - " + error.getKey() + ": " + error.getMessage())
            );
        }
    }
}

