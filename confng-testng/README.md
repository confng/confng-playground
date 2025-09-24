# ConfNG TestNG Integration Examples

This subproject demonstrates the new TestNG support features in ConfNG version 1.0.1-SNAPSHOT. It showcases advanced TestNG capabilities integrated with ConfNG configuration management.

## Features Demonstrated

### 1. ConfNGTestNGIntegrationTest
- **TestNG Annotations**: @BeforeSuite, @BeforeClass, @BeforeMethod, @AfterMethod, @AfterClass, @AfterSuite
- **Data Providers**: Parallel and sequential data providers with ConfNG configurations
- **Test Groups**: Organized tests into logical groups (app-config, database-config, security, etc.)
- **Test Dependencies**: Tests that depend on other groups or methods
- **Soft Assertions**: Using TestNG SoftAssert for multiple validations
- **AssertJ Integration**: Fluent assertions with AssertJ library
- **Performance Testing**: Load testing with invocationCount and threadPoolSize
- **Sensitive Data Handling**: Testing masked sensitive configuration values

### 2. ConfNGParallelExecutionTest
- **Parallel Execution**: Tests running in parallel with thread pools
- **Thread Safety**: Validating ConfNG thread safety under concurrent access
- **Concurrent Collections**: Using ConcurrentHashMap and AtomicInteger for thread-safe counters
- **Batch Processing**: Simulating batch operations with configuration
- **Thread Pool Configuration**: Dynamic thread pool sizing based on configuration

### 3. ConfNGAdvancedFeaturesTest
- **Method Interceptors**: @BeforeMethod and @AfterMethod with method reflection
- **Data-Driven Testing**: Comprehensive data provider examples
- **Configuration Validation**: Custom validation logic and edge cases
- **Boolean Conversion**: Testing various boolean string representations
- **Stress Testing**: High-volume parallel execution for performance validation

## TestNG Configuration

The project includes a comprehensive `testng.xml` configuration file that demonstrates:
- **Suite-level parallel execution**
- **Test-level thread configuration**
- **Group inclusion/exclusion**
- **Parameter passing**
- **Test ordering and dependencies**

## Key ConfNG Features Showcased

1. **Configuration Retrieval**: `ConfNG.get()`, `ConfNG.getInt()`, `ConfNG.getBoolean()`
2. **Sensitive Data Masking**: `ConfNG.getForDisplay()` for secure logging
3. **Default Value Handling**: Automatic fallback to default values
4. **Thread Safety**: Concurrent access validation
5. **Performance**: High-throughput configuration access

## Running the Tests

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Groups
```bash
./gradlew test -Dgroups="confng-integration"
./gradlew test -Dgroups="parallel-execution"
./gradlew test -Dgroups="advanced-features"
```

### Run with TestNG XML
```bash
./gradlew test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run with Parallel Execution
```bash
./gradlew test -Dparallel=methods -DthreadCount=10
```

## Dependencies

- **ConfNG**: 1.0.1-SNAPSHOT (latest version with TestNG support)
- **TestNG**: 7.11.0 (latest stable version)
- **AssertJ**: 3.24.2 (for fluent assertions)
- **SLF4J**: 2.0.7 (for logging)

## Configuration Examples

The tests demonstrate various configuration patterns:

### Environment Configuration
```java
public enum TestConfig implements ConfNGKey {
    APP_NAME("app.name"),
    APP_VERSION("app.version"),
    APP_ENVIRONMENT("app.environment");
    // ...
}
```

### Sensitive Data Configuration
```java
@Override
public boolean isSensitive() {
    return this == DB_PASSWORD || this == API_KEY;
}
```

### Default Value Handling
```java
@Override
public String getDefaultValue() {
    return switch (this) {
        case APP_NAME -> "ConfNG TestNG Example";
        case APP_VERSION -> "1.0.0";
        default -> null;
    };
}
```

## Best Practices Demonstrated

1. **Test Organization**: Logical grouping of tests by functionality
2. **Resource Management**: Proper setup and cleanup in @BeforeClass/@AfterClass
3. **Parallel Safety**: Thread-safe test design for parallel execution
4. **Performance Testing**: Realistic load testing scenarios
5. **Error Handling**: Comprehensive validation and edge case testing
6. **Documentation**: Clear test descriptions and meaningful assertions

## Integration with CI/CD

The TestNG configuration supports various CI/CD scenarios:
- Parallel execution for faster builds
- Group-based test selection for different environments
- Comprehensive reporting with TestNG's built-in reporters
- Performance benchmarking capabilities

This subproject serves as a comprehensive reference for integrating ConfNG with TestNG in real-world applications.