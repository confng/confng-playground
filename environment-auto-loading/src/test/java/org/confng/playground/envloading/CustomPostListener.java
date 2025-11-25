package org.confng.playground.envloading;

import org.confng.ConfNG;
import org.confng.testng.ConfNGListener;
import org.testng.ISuite;
import org.testng.ITestResult;

/**
 * Example custom listener that executes AFTER TestNGParameterListener.
 *
 * <p>This listener demonstrates how to add custom post-processing logic
 * after ConfNG has loaded all configuration files.</p>
 *
 * <p>Priority: 10 (positive priority executes after TestNGParameterListener which has priority 0)</p>
 */
public class CustomPostListener implements ConfNGListener {

    @Override
    public int getPriority() {
        return 10; // Execute after TestNGParameterListener (priority 0)
    }

    @Override
    public void onSuiteStart(ISuite suite) {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Custom Post-Listener (Priority: 10)                      ║");
        System.out.println("║  Executing AFTER ConfNG loads configuration                ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Example: Validate configuration, set up test data, etc.
        System.out.println("[CustomPostListener] Configuration has been loaded!");
        System.out.println("[CustomPostListener] Validating configuration...");
        
        // Access loaded configuration
        String environment = ConfNG.getEnvironmentName();
        System.out.println("[CustomPostListener] Detected environment: " + environment);
        
        System.out.println("[CustomPostListener] Setting up test data for environment: " + environment);
        System.out.println("[CustomPostListener] Post-processing complete!");
        System.out.println();
    }

    @Override
    public void onSuiteFinish(ISuite suite) {
        System.out.println();
        System.out.println("[CustomPostListener] Tearing down test data...");
        System.out.println("[CustomPostListener] Teardown complete!");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[CustomPostListener] Test starting: " + result.getName());
    }
}

