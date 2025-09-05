# System Properties Example

This example demonstrates how to use ConfNG with Java system properties as configuration source.

## Features Demonstrated

- Automatic system property loading
- Built-in Java system properties access
- Runtime system property modification
- Default values for missing properties
- Type conversion from system properties
- System property precedence

## System Properties Usage

ConfNG automatically reads Java system properties without any explicit configuration.

### Setting System Properties

**Via Command Line:**
```bash
java -Dapp.name="My App" -Dwebdriver.browser=firefox -jar myapp.jar
```

**Via Gradle:**
```gradle
test {
    systemProperty 'app.name', 'ConfNG System Properties Example'
    systemProperty 'webdriver.browser', 'firefox'
}
```

**Programmatically:**
```java
System.setProperty("app.name", "My App");
```

## Key Features

### Automatic System Property Loading
```java
// No explicit loading needed - ConfNG automatically reads system properties
String appName = ConfNG.get(SystemConfig.APP_NAME);
```

### Built-in Java Properties
```java
public enum SystemConfig implements ConfNGKey {
    JAVA_VERSION("java.version"),    // Built-in Java property
    USER_HOME("user.home"),          // Built-in Java property
    OS_NAME("os.name");              // Built-in Java property
}

String javaVersion = ConfNG.get(SystemConfig.JAVA_VERSION);
```

### Default Values
```java
public enum SystemConfig implements ConfNGKey {
    WEBDRIVER_BROWSER("webdriver.browser", "chrome"),  // Defaults to "chrome"
    TEST_THREADS("test.threads", "1");                 // Defaults to "1"
}
```

### Runtime Modification
```java
// System properties can be changed at runtime
System.setProperty("webdriver.browser", "firefox");
String browser = ConfNG.get(SystemConfig.WEBDRIVER_BROWSER); // Returns "firefox"
```

### Type Conversion
```java
// System properties are strings, but ConfNG converts them
Boolean parallel = ConfNG.getBoolean(SystemConfig.TEST_PARALLEL);  // "true" -> true
Integer threads = ConfNG.getInt(SystemConfig.TEST_THREADS);        // "4" -> 4
```

## Common Use Cases

### Test Configuration
```bash
# Run tests with specific browser
./gradlew test -Dwebdriver.browser=firefox -Dwebdriver.headless=true

# Run parallel tests
./gradlew test -Dtest.parallel=true -Dtest.threads=4
```

### Environment-specific Configuration
```bash
# Development
java -Dapp.profile=dev -Ddatabase.url=jdbc:h2:mem:devdb -jar app.jar

# Production
java -Dapp.profile=prod -Ddatabase.url=jdbc:postgresql://prod:5432/db -jar app.jar
```

### CI/CD Integration
```yaml
# GitHub Actions
- name: Run Tests
  run: ./gradlew test -Dwebdriver.headless=true -Dreport.output.dir=./ci-reports
```

## Running the Example

```bash
cd examples/system-properties
./gradlew test
```

You can also override properties:

```bash
./gradlew test -Dwebdriver.browser=edge -Dtest.threads=8
```

## Expected Output

All tests should pass, demonstrating:
- ✅ System property loading and access
- ✅ Built-in Java system properties
- ✅ Default value fallback
- ✅ Runtime property modification
- ✅ Type conversion from string properties