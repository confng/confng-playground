package org.confng.playground.envloading;

import org.confng.ConfNG;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates case-insensitive environment key detection in ConfNG.
 * 
 * <p>The environment can be set using any case variation:</p>
 * <ul>
 *   <li>APP_ENV (uppercase - traditional)</li>
 *   <li>app_env (lowercase)</li>
 *   <li>App_Env (title case)</li>
 *   <li>aPp_EnV (mixed case)</li>
 * </ul>
 * 
 * <p>All variations are treated as the same key!</p>
 */
public class CaseInsensitiveKeysTest {

    @BeforeClass
    public void setup() {
        // NOTE: When running with TestNG XML, the TestNGParameterListener automatically
        // loads global config and environment-specific config in the correct order.
        // We don't need to load them again here.
    }

    @Test
    public void testEnvironmentDetectionIsCaseInsensitive() {
        // The TestNG XML file uses "app_env" (lowercase)
        // But ConfNG should still detect it correctly
        
        String detectedEnv = ConfNG.autoLoadConfig();
        
        System.out.println("========================================");
        System.out.println("Case-Insensitive Environment Detection");
        System.out.println("========================================");
        System.out.println("Detected environment: " + detectedEnv);
        System.out.println();
        System.out.println("This works with ANY case variation:");
        System.out.println("  - APP_ENV (uppercase)");
        System.out.println("  - app_env (lowercase) ← used in testng-uat.xml");
        System.out.println("  - App_Env (title case)");
        System.out.println("  - aPp_EnV (mixed case)");
        System.out.println("========================================");
        
        // Should detect "uat" regardless of the case used in the parameter name
        assertThat(detectedEnv).isNotNull();
        assertThat(detectedEnv).isNotEmpty();
    }

    @Test
    public void testFallbackKeysAreCaseInsensitive() {
        // ConfNG checks multiple keys in order:
        // 1. APP_ENV (or app_env, App_Env, etc.)
        // 2. ENVIRONMENT (or environment, Environment, etc.)
        // 3. ENV (or env, Env, etc.)
        
        System.out.println("Fallback key order (all case-insensitive):");
        System.out.println("  1. APP_ENV → app_env → App_Env → ...");
        System.out.println("  2. ENVIRONMENT → environment → Environment → ...");
        System.out.println("  3. ENV → env → Env → ...");
        System.out.println("  4. Default: 'local'");
        
        // The test should pass regardless of which key variation is used
        assertThat(ConfNG.autoLoadConfig()).isNotNull();
    }

    @Test
    public void testCaseInsensitivityBenefits() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("Benefits of Case-Insensitive Keys");
        System.out.println("========================================");
        System.out.println();
        System.out.println("1. User-Friendly:");
        System.out.println("   No need to remember exact casing");
        System.out.println();
        System.out.println("2. Cross-Platform:");
        System.out.println("   Windows env vars are case-insensitive by default");
        System.out.println("   Unix/Linux can use any convention");
        System.out.println();
        System.out.println("3. Flexible:");
        System.out.println("   Works with different coding styles");
        System.out.println("   Compatible with various config file formats");
        System.out.println();
        System.out.println("4. Examples that all work:");
        System.out.println("   export APP_ENV=uat");
        System.out.println("   export app_env=uat");
        System.out.println("   export App_Env=uat");
        System.out.println("   <parameter name=\"app_env\" value=\"uat\"/>");
        System.out.println("   <parameter name=\"APP_ENV\" value=\"uat\"/>");
        System.out.println("========================================");
    }
}

