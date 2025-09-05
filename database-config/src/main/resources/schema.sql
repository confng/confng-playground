-- Configuration table schema
CREATE TABLE IF NOT EXISTS app_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(255) NOT NULL UNIQUE,
    config_value TEXT,
    environment VARCHAR(50) DEFAULT 'default',
    is_sensitive BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) DEFAULT 'system',
    description TEXT
);

-- Insert sample configuration data
INSERT INTO app_config (config_key, config_value, environment, is_sensitive, description) VALUES
-- Application settings
('app.name', 'ConfNG Database Example', 'default', FALSE, 'Application name'),
('app.version', '3.0.0', 'default', FALSE, 'Application version'),
('app.debug', 'false', 'default', FALSE, 'Debug mode flag'),

-- Database settings
('database.pool.min-size', '5', 'default', FALSE, 'Minimum connection pool size'),
('database.pool.max-size', '20', 'default', FALSE, 'Maximum connection pool size'),
('database.timeout', '30000', 'default', FALSE, 'Database timeout in milliseconds'),

-- API settings
('api.rate-limit', '1000', 'default', FALSE, 'API rate limit per hour'),
('api.timeout', '15000', 'default', FALSE, 'API timeout in milliseconds'),

-- Sensitive settings
('api.secret-key', 'sk-secret-database-key-12345', 'default', TRUE, 'API secret key'),
('encryption.key', 'db-encryption-key-xyz789', 'default', TRUE, 'Data encryption key'),

-- Environment-specific settings
('app.debug', 'true', 'development', FALSE, 'Debug mode for development'),
('database.pool.max-size', '50', 'production', FALSE, 'Production max pool size'),
('api.rate-limit', '10000', 'production', FALSE, 'Production API rate limit'),

-- Feature flags
('features.new-ui', 'true', 'default', FALSE, 'Enable new UI features'),
('features.beta-api', 'false', 'default', FALSE, 'Enable beta API endpoints'),
('features.analytics', 'true', 'production', FALSE, 'Enable analytics in production');

-- Create index for better performance
CREATE INDEX IF NOT EXISTS idx_config_key_env ON app_config(config_key, environment);
CREATE INDEX IF NOT EXISTS idx_environment ON app_config(environment);