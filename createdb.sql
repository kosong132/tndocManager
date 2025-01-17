CREATE DATABASE tn_db;

USE tn_db;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
use tn_db;
INSERT INTO users (username, password, email) 
VALUES 
('koksiong', '000371TN', 'koksiong.yong@tiongnam.com.my'),
('Ivan', '123456TN', 'ivanchu@tiongnam.com.my');
