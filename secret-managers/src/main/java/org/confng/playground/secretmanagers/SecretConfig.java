package org.confng.playground.secretmanagers;

import org.confng.api.ConfNGKey;

/**
 * Secret manager configuration enum.
 * This enum defines all configuration keys used in the secret managers module.
 * 
 * @author Bharat Kumar Malviya
 * @author GitHub: github.com/imBharatMalviya
 * @version 1.0
 * @since 2025
 */
public enum SecretConfig implements ConfNGKey {
    // Database credentials
    DB_PASSWORD("database/password", null, true),
    DB_CONNECTION_STRING("database/connection-string", null, true),
    
    // API keys
    SERVICE1_API_KEY("api-keys/service1", null, true),
    SERVICE2_API_KEY("api-keys/service2", null, true),

    // OAuth secrets
    OAUTH_CLIENT_SECRET("oauth/client-secret", null, true),
    JWT_SIGNING_KEY("oauth/jwt-signing-key", null, true),

    // Cloud provider credentials
    CLOUD_ACCESS_KEY("cloud/access-key", null, true),
    CLOUD_SECRET_KEY("cloud/secret-key", null, true),
    
    // Non-sensitive configuration (can also be stored in secret manager)
    APP_VERSION("app/version", "1.0.0"),
    FEATURE_FLAGS("app/feature-flags", "{}");

    private final String key;
    private final String defaultValue;
    private final boolean sensitive;

    SecretConfig(String key, String defaultValue) {
        this(key, defaultValue, false);
    }

    SecretConfig(String key, String defaultValue, boolean sensitive) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.sensitive = sensitive;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isSensitive() {
        return sensitive;
    }
}
