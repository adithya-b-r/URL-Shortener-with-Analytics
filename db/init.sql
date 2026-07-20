CREATE DATABASE IF NOT EXISTS url_shortener;

USE url_shortener;

CREATE TABLE IF NOT EXISTS urls(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  short_code VARCHAR(10) UNIQUE,
  long_url TEXT NOT NULL,
  created_at TIMESTAMP,
  expires_at TIMESTAMP,
  click_count BIGINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS clicks(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  url_id BIGINT NOT NULL,
  clicked_at TIMESTAMP NOT NULL,
  ip_address VARCHAR(50) NOT NULL,
  user_agent TEXT NOT NULL,
  country VARCHAR(50),
  device_type VARCHAR(50),

  CONSTRAINT fk_url
    FOREIGN KEY (url_id)
    REFERENCES urls(id)
    ON DELETE CASCADE
);