-- MySQL initialization script for ShedLock Demo
-- This script runs when the MySQL container starts for the first time

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS shedlock_demo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Flush privileges to apply changes
FLUSH PRIVILEGES;

-- Use the database
USE shedlock_demo;

-- Create initial tables (Liquibase will handle the actual schema)
-- This is just a placeholder to ensure the database is ready

-- Show the created database
SHOW DATABASES; 