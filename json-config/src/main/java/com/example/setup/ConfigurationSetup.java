package com.example.setup;

/**
 * Utility class for setting up JSON configuration for testing using ConfNG framework.
 * This class provides static methods to initialize system properties for test scenarios.
 * 
 * @author Bharat Kumar Malviya GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public class ConfigurationSetup {
    
    public static void setupJsonConfiguration() {
        // Set system properties to simulate JSON configuration
        System.setProperty("app.name", "ConfNG JSON Example");
        System.setProperty("app.version", "1.0.0");
        System.setProperty("app.debug", "true");
        
        // Database configuration
        System.setProperty("database.url", "jdbc:h2:mem:testdb");
        System.setProperty("database.username", "sa");
        System.setProperty("database.password", "");
        System.setProperty("database.maxConnections", "10");
        
        // WebDriver configuration
        System.setProperty("webdriver.browser", "chrome");
        System.setProperty("webdriver.headless", "false");
        System.setProperty("webdriver.timeout", "30");
        System.setProperty("webdriver.implicitWait", "10");
        
        // API configuration
        System.setProperty("api.baseUrl", "https://api.example.com");
        System.setProperty("api.timeout", "5000");
        System.setProperty("api.retries", "3");
    }
}