# Environment Variables Example

This example demonstrates how to use ConfNG with environment variables as configuration source.

## Features Demonstrated

- Automatic environment variable loading
- Environment variable naming conventions
- Default values for missing environment variables
- Sensitive environment variable handling
- Type conversion from environment variables

## Environment Variable Mapping

ConfNG automatically maps configuration keys to environment variables:

| Configuration Key | Environment Variable | Example Value |
|-------------------|---------------------|---------------|
| `app.name` | `APP_NAME` | `ConfNG Environment Example` |
| `database.url` | `DATABASE_URL` | `jdbc:postgresql://localhost:5432/testdb` |
| `api.key` | `API_KEY` | `sk-test-key-12345` |

## Key Features

### Automatic Environment Variable Loading
```java
// No explicit loading needed - ConfNG automatically reads environment variables
String appName = ConfNG.get(EnvConfig.APP_NAME);
```

### Default Values
```java
public enum EnvConfig implements ConfNGKey {
    APP_NAME("app.name", "default-app"),  // Falls back to "default-app" if APP_NAME not set
    DATABASE_URL("database.url", "jdbc:h2:mem:testdb");
}
```

### Sensitive Data Protection
```java
public enum EnvConfig implements ConfNGKey {
    API_KEY("api.key", null, true),  // Marked as sensitive
    DATABASE_PASSWORD("database.password", "", true);
}

// Sensitive values are masked
String masked = ConfNG.getForDisplay(EnvConfig.API_KEY); // Returns "***"
```

### Type Conversion
```java
// Environment variables are strings, but ConfNG converts them
Boolean debug = ConfNG.getBoolean(EnvConfig.APP_DEBUG);  // "true" -> true
Integer timeout = ConfNG.getInt(EnvConfig.API_TIMEOUT);  // "10000" -> 10000
```

## Running the Example

The example sets environment variables in the Gradle build file for testing:

```bash
cd examples/env-variables
./gradlew test
```

## Real-world Usage

In production, set environment variables before running your application:

```bash
export APP_NAME="My Production App"
export DATABASE_URL="jdbc:postgresql://prod-db:5432/myapp"
export API_KEY="sk-prod-key-xyz"
java -jar myapp.jar
```

Or use Docker:

```dockerfile
ENV APP_NAME="My Production App"
ENV DATABASE_URL="jdbc:postgresql://prod-db:5432/myapp"
ENV API_KEY="sk-prod-key-xyz"
```

## Expected Output

All tests should pass, demonstrating:
- ✅ Environment variable loading and mapping
- ✅ Default value fallback
- ✅ Type conversion from string environment variables
- ✅ Sensitive data masking