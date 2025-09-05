# JSON Configuration Example

This example demonstrates how to use ConfNG with JSON configuration files.

## Features Demonstrated

- Loading configuration from JSON files
- Type-safe configuration keys with enums
- Different data types (String, Integer, Boolean)
- Sensitive data handling and masking
- Nested JSON structure access

## Configuration Structure

The example uses a `config.json` file with nested configuration:

```json
{
  "app": {
    "name": "ConfNG JSON Example",
    "version": "1.0.0",
    "debug": true
  },
  "database": {
    "url": "jdbc:h2:mem:testdb",
    "username": "sa",
    "password": "",
    "maxConnections": 10
  },
  "webdriver": {
    "browser": "chrome",
    "headless": false,
    "timeout": 30
  }
}
```

## Key Features

### Enum-based Configuration Keys
```java
public enum AppConfig implements ConfNGKey {
    APP_NAME("app.name"),
    DB_URL("database.url"),
    WEBDRIVER_BROWSER("webdriver.browser");
    // ...
}
```

### Type-safe Access
```java
String appName = ConfNG.get(AppConfig.APP_NAME);
Integer timeout = ConfNG.getInt(AppConfig.WEBDRIVER_TIMEOUT);
Boolean debug = ConfNG.getBoolean(AppConfig.APP_DEBUG);
```

### Sensitive Data Protection
```java
// Password is marked as sensitive and gets masked
String maskedPassword = ConfNG.getForDisplay(AppConfig.DB_PASSWORD); // Returns "***"
```

## Running the Example

```bash
cd examples/json-config
./gradlew test
```

## Expected Output

All tests should pass, demonstrating:
- ✅ JSON configuration loading
- ✅ Type conversion (String, Integer, Boolean)
- ✅ Nested property access using dot notation
- ✅ Sensitive data masking