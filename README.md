# ConfNG Examples

This directory contains practical examples demonstrating different configuration sources and use cases with ConfNG.

## Available Examples

### 📄 [JSON Configuration](json-config/)
Basic example using JSON files for configuration with TestNG tests. Perfect for getting started with ConfNG.

### 🌍 [Environment Variables](env-variables/)
Example showing how to use environment variables as configuration source. Ideal for containerized applications and CI/CD.

### ⚙️ [System Properties](system-properties/)
Demonstrates using Java system properties for configuration. Great for command-line configuration and testing.

### 🔐 [Secret Managers](secret-managers/)
Advanced example integrating with cloud secret management services (AWS Secrets Manager, Azure Key Vault, HashiCorp Vault).

### 🗄️ [Database Configuration](database-config/)
Example using database as a configuration source with custom implementation. Enables dynamic configuration and feature flags.

## Running Examples

Each example is a standalone Gradle project that can be run independently:

```bash
cd examples/json-config
./gradlew test
```

## Prerequisites

- Java 11 or higher
- Gradle 7.0 or higher

## Example Structure

Each example follows this structure:
```
example-name/
├── build.gradle
├── src/
│   └── test/java/
│       └── com/example/
│           └── ExampleTest.java
├── src/
│   └── main/resources/
│       └── config files
└── README.md
```