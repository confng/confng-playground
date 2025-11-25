package org.confng.playground.envloading;

import org.confng.testng.ConfNGListener;
import org.testng.ISuite;
import org.testng.ITestResult;

/**
 * Example custom listener that executes BEFORE TestNGParameterListener.
 *
 * <p>This listener demonstrates how to add custom pre-processing logic
 * before ConfNG loads configuration files.</p>
 *
 * <p>Priority: -10 (negative priority executes before TestNGParameterListener which has priority 0)</p>
 */
public class CustomPreListener implements ConfNGListener {

    @Override
    public int getPriority() {
        return -10; // Execute before TestNGParameterListener (priority 0)
    }

    @Override
    public void onSuiteStart(ISuite suite) {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║  Custom Pre-Listener (Priority: -10)                      ║");
        System.out.println("║  Executing BEFORE ConfNG loads configuration               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Example: Set up test environment, initialize resources, etc.
        System.out.println("[CustomPreListener] Setting up test environment...");
        System.out.println("[CustomPreListener] Initializing test resources...");
        System.out.println("[CustomPreListener] Pre-processing complete!");
        System.out.println();
    }

    @Override
    public void onSuiteFinish(ISuite suite) {
        System.out.println();
        System.out.println("[CustomPreListener] Cleaning up test environment...");
        System.out.println("[CustomPreListener] Cleanup complete!");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[CustomPreListener] Test starting: " + result.getName());
    }
}

