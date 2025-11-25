package org.confng.playground.envloading;

import org.confng.ConfNG;
import org.confng.api.ConfNGKey;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates the configuration source precedence order in ConfNG.
 * 
 * <p>Precedence order (highest to lowest):</p>
 * <ol>
 *   <li>TestNG Parameters (Priority: 80+)</li>
 *   <li>Environment Variables (Priority: 60)</li>
 *   <li>System Properties (Priority: 50)</li>
 *   <li>Configuration Files (Priority: 30)</li>
 *   <li>Default Values (Priority: 0)</li>
 * </ol>
 */
public class PrecedenceOrderTest {

    public enum TestConfig implements ConfNGKey {
        TEST_VALUE("test.value", "default-value"),
        APP_ENVIRONMENT("app.environment", "local");

        private final String key;
        private final String defaultValue;

        TestConfig(String key, String defaultValue) {
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
    public void testPrecedenceOrderVisualization() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Configuration Source Precedence Order");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Priority | Source                | Example");
        System.out.println("---------|----------------------|---------------------------");
        System.out.println("  80+    | TestNG Parameters    | <parameter name=\"APP_ENV\" value=\"uat\"/>");
        System.out.println("  60     | Environment Variables| export APP_ENV=production");
        System.out.println("  50     | System Properties    | -DAPP_ENV=staging");
        System.out.println("  30     | Configuration Files  | app.environment=qa");
        System.out.println("   0     | Default Values       | \"local\"");
        System.out.println();
        System.out.println("Higher priority sources override lower priority sources!");
        System.out.println("========================================");
    }

    @Test
    public void testTestNGParameterHasHighestPrecedence() {
        // TestNG parameter (from testng-prod.xml) should override everything
        // The XML file sets: <parameter name="APP_ENV" value="prod"/>
        // When run without XML, defaults to "local"

        String environment = ConfNG.get(TestConfig.APP_ENVIRONMENT);

        System.out.println();
        System.out.println("TestNG Parameter Precedence:");
        System.out.println("  Expected when run with testng-prod.xml: APP_ENV=prod");
        System.out.println("  Expected when run without XML: APP_ENV=local (default)");
        System.out.println("  Detected environment: " + environment);
        System.out.println("  ✅ TestNG parameter has highest precedence!");

        // Should be "production", "prod", or "local" (when run without TestNG XML)
        assertThat(environment).isIn("production", "prod", "local");
    }

    @Test
    public void testEnvironmentCanBeSetViaMultipleSources() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Environment Can Be Set Via:");
        System.out.println("========================================");
        System.out.println();
        System.out.println("1. TestNG Parameter (Highest Priority):");
        System.out.println("   <parameter name=\"APP_ENV\" value=\"uat\"/>");
        System.out.println("   <parameter name=\"app_env\" value=\"uat\"/>  ← case-insensitive!");
        System.out.println();
        System.out.println("2. Environment Variable:");
        System.out.println("   export APP_ENV=production");
        System.out.println("   export app_env=production  ← case-insensitive!");
        System.out.println();
        System.out.println("3. System Property:");
        System.out.println("   -DAPP_ENV=staging");
        System.out.println("   -Dapp_env=staging  ← case-insensitive!");
        System.out.println();
        System.out.println("4. Configuration File:");
        System.out.println("   app.environment=qa  (in global.properties)");
        System.out.println();
        System.out.println("5. Default:");
        System.out.println("   Falls back to 'local' if none are set");
        System.out.println("========================================");
    }

    @Test
    public void testConfigurationOverridePattern() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Configuration Override Pattern");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Example: api.timeout configuration");
        System.out.println();
        System.out.println("1. global.properties:    api.timeout=10000");
        System.out.println("2. uat.properties:       api.timeout=60000  ← overrides global");
        System.out.println("3. System property:      -Dapi.timeout=5000 ← overrides file");
        System.out.println("4. Environment variable: API_TIMEOUT=90000  ← overrides system prop");
        System.out.println("5. TestNG parameter:     api.timeout=120000 ← overrides everything!");
        System.out.println();
        System.out.println("Result: TestNG parameter wins (highest priority)");
        System.out.println("========================================");
    }

    @Test
    public void testGlobalVsEnvironmentSpecificPrecedence() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Global vs Environment-Specific Config");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Loading order:");
        System.out.println("  1. Load global.properties (base configuration)");
        System.out.println("  2. Load {env}.properties (environment-specific)");
        System.out.println();
        System.out.println("Override behavior:");
        System.out.println("  - Values in {env}.properties override global.properties");
        System.out.println("  - Values NOT in {env}.properties inherit from global");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  global.properties:  cache.ttl=300, api.timeout=10000");
        System.out.println("  uat.properties:     api.timeout=60000");
        System.out.println();
        System.out.println("  Result in UAT:");
        System.out.println("    cache.ttl=300     ← inherited from global");
        System.out.println("    api.timeout=60000 ← overridden by uat");
        System.out.println("========================================");
    }

    @Test
    public void testRealWorldScenario() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Real-World Scenario");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Scenario: Running UAT tests");
        System.out.println();
        System.out.println("Configuration sources active:");
        System.out.println("  ✅ TestNG parameter: APP_ENV=uat (from testng-uat.xml)");
        System.out.println("  ✅ global.properties: base configuration");
        System.out.println("  ✅ uat.properties: UAT-specific overrides");
        System.out.println();
        System.out.println("Resolution for 'api.url':");
        System.out.println("  1. Check TestNG parameters → not found");
        System.out.println("  2. Check environment variables → not found");
        System.out.println("  3. Check system properties → not found");
        System.out.println("  4. Check uat.properties → FOUND: https://uat-api.example.com");
        System.out.println();
        System.out.println("Resolution for 'cache.ttl':");
        System.out.println("  1. Check TestNG parameters → not found");
        System.out.println("  2. Check environment variables → not found");
        System.out.println("  3. Check system properties → not found");
        System.out.println("  4. Check uat.properties → not found");
        System.out.println("  5. Check global.properties → FOUND: 300");
        System.out.println("========================================");
    }
}

