-- Initial Schema for SpiderSync Search Engine
-- This migration creates the base tables with optimized indexes

-- Pages table with full-text search support
CREATE TABLE IF NOT EXISTS pages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(768) NOT NULL UNIQUE,
    title VARCHAR(500) NOT NULL,
    description VARCHAR(1000),
    content MEDIUMTEXT,
    keywords VARCHAR(500),
    language VARCHAR(10) DEFAULT 'en',
    content_hash VARCHAR(64),
    status_code INT DEFAULT 200,
    response_time INT,
    crawl_depth INT DEFAULT 0,
    page_rank DOUBLE DEFAULT 0.0,
    last_crawled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_url (url(255)),
    INDEX idx_title (title(255)),
    INDEX idx_language (language),
    INDEX idx_last_crawled (last_crawled_at),
    INDEX idx_page_rank (page_rank),
    FULLTEXT INDEX ft_content (content),
    FULLTEXT INDEX ft_title_content (title, content)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crawl Jobs table
CREATE TABLE IF NOT EXISTS crawl_jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seed_url VARCHAR(768) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    pages_crawled INT DEFAULT 0,
    pages_indexed INT DEFAULT 0,
    errors_count INT DEFAULT 0,
    started_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Search Queries Analytics table
CREATE TABLE IF NOT EXISTS search_queries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    query_text VARCHAR(500) NOT NULL,
    results_count INT DEFAULT 0,
    user_id BIGINT NULL,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    clicked_result_id BIGINT NULL,
    click_position INT NULL,
    search_time_ms INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_query_text (query_text(255)),
    INDEX idx_created_at (created_at),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    api_key VARCHAR(64) UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_api_key (api_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default admin user (password: admin123 - hashed with BCrypt)
-- Note: Change this password in production!
INSERT INTO users (username, email, password, role, api_key) 
VALUES (
    'admin', 
    'admin@spidersync.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'ADMIN',
    'admin-api-key-change-this-in-production'
) ON DUPLICATE KEY UPDATE username=username;
