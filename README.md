# ConfNG Playground Examples

This repository contains standalone examples demonstrating different configuration sources and use cases with ConfNG. Each example is a complete, independent Gradle project.

## 🚀 Quick Start

Each example can be run independently:

```bash
cd <example-directory>
./gradlew test
```

## Available Examples

### 📄 [JSON Configuration](json-config/)
Basic example using JSON files for configuration with TestNG tests. Perfect for getting started with ConfNG.

### 🌍 [Environment Variables](env-variables/)
Example showing how to use environment variables as configuration source. Ideal for containerized applications and CI/CD.

### ⚙️ [System Properties](system-properties/)
Demonstrates using Java system properties for configuration. Great for command-line configuration and testing.

### 📋 [Properties Configuration](properties-config/)
Traditional Java properties file configuration with multiple file support and precedence handling.

### 🔗 [Multi-Source Configuration](multi-source-config/)
Advanced example demonstrating multiple configuration sources working together with proper precedence ordering.

### 📝 [YAML Configuration](yaml-config/)
Custom YAML configuration source implementation showing how to extend ConfNG with new formats.

### 🧪 [TestNG Integration](confng-testng/)
Comprehensive TestNG integration examples with advanced testing patterns and parallel execution.

### 🔐 [Secret Managers](secret-managers/)
Advanced example integrating with cloud secret management services (AWS Secrets Manager, Azure Key Vault, HashiCorp Vault).

### 🗄️ [Database Configuration](database-config/)
Example using database as a configuration source with custom implementation. Enables dynamic configuration and feature flags.

## Running Examples

### Individual Example
```bash
cd properties-config
./gradlew test
```

### With Custom Configuration
```bash
cd multi-source-config
export APP_NAME="My Custom App"
./gradlew test -Dapp.environment=production
```

## Prerequisites

- Java 11 or higher
- Gradle 7.0 or higher (wrapper included in each project)
- ConfNG 1.0.1 (automatically downloaded from Maven Central)

## Example Structure

Each example is a standalone Gradle project:
```
example-name/
├── build.gradle                 # Standalone Gradle build
├── gradlew                      # Gradle wrapper script
├── gradlew.bat                  # Gradle wrapper (Windows)
├── gradle/                      # Gradle wrapper files
├── src/
│   ├── main/java/               # Source code (if any)
│   ├── test/java/               # Test classes
│   └── test/resources/          # Configuration files
└── README.md                    # Example-specific documentation
```
