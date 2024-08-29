-- Tao databasea
CREATE DATABASE ghtk_ecom;
USE ghtk_ecom;
-- Tao bang
CREATE TABLE IF NOT EXISTS `user` (
                                      ID INT AUTO_INCREMENT PRIMARY KEY,
                                      `name` VARCHAR(255) NOT NULL,
                                      email VARCHAR(255) NOT NULL UNIQUE,
                                      `password` VARCHAR(255) NOT NULL,
                                      first_name VARCHAR(255),
                                      last_name VARCHAR(255),
                                      phone_number VARCHAR(20),
                                      `status` ENUM('ACTIVE', 'INACTIVE','BLOCKED') DEFAULT 'INACTIVE',
									  created_date_time DATETIME NOT NULL,
                                      last_change_password_date_time DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS address (
                                       ID INT AUTO_INCREMENT PRIMARY KEY,
                                       `name` VARCHAR(255) NOT NULL,
                                       `type` VARCHAR(255) NOT NULL,
                                       address_parentID INT NULL,
                                       created_user_id INT
);

CREATE TABLE IF NOT EXISTS seller (
                                      ID INT AUTO_INCREMENT PRIMARY KEY,
                                      user_id INT NOT NULL UNIQUE,
                                      `name` VARCHAR(255) UNIQUE,
                                      rating FLOAT NOT NULL DEFAULT 0,
                                      follow INT NOT NULL DEFAULT 0,
                                      address_id INT NOT NULL,
                                      address_detail VARCHAR(500),
                                      image_path VARCHAR(255),
                                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      deleted_at TIMESTAMP NULL,
                                      FOREIGN KEY (user_id) REFERENCES `user`(ID),
                                      FOREIGN KEY (address_id) REFERENCES address(ID)
);

CREATE TABLE IF NOT EXISTS user_address (
                                            ID INT AUTO_INCREMENT PRIMARY KEY,
                                            `name` VARCHAR(255) NOT NULL,
                                            `phone_number` VARCHAR(10) NOT NULL,
                                            address_id INT NOT NULL,
                                            address_detail VARCHAR(500),
                                            user_id INT NOT NULL,
                                            is_default BOOLEAN,
                                            FOREIGN KEY (address_id) REFERENCES address(ID),
                                            FOREIGN KEY (user_id) REFERENCES `user`(ID)
);

CREATE TABLE IF NOT EXISTS category (
                                        ID INT AUTO_INCREMENT PRIMARY KEY,
                                        `name` VARCHAR(255) NOT NULL,
                                        `description` TEXT NULL,
                                        parentID INT NULL,
                                        image VARCHAR(255) NULL,
                                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        deleted_at TIMESTAMP NULL,
                                        created_by_user INT
);

CREATE TABLE IF NOT EXISTS product (
                                       ID INT AUTO_INCREMENT PRIMARY KEY,
                                       `name` VARCHAR(255) NOT NULL,
                                       `description` TEXT,
                                       seller_id INT,
                                       category_id INT,
                                       slug VARCHAR(255),
                                       `status` ENUM('SHOW', 'HIDE') DEFAULT 'SHOW',
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       deleted_at TIMESTAMP NULL,
                                       price INT,
                                       quantity INT,
                                       sold INT DEFAULT 0,
                                       created_by_user INT,
                                       FOREIGN KEY (seller_id) REFERENCES `user`(ID),
                                       FOREIGN KEY (category_id) REFERENCES category(ID)
);




CREATE TABLE IF NOT EXISTS product_asset (
                                             ID INT AUTO_INCREMENT PRIMARY KEY,
                                             `name` VARCHAR(255),
                                             product_id INT,
                                             `default` BOOLEAN DEFAULT FALSE,
                                             FOREIGN KEY (product_id) REFERENCES product(ID)
);

CREATE TABLE IF NOT EXISTS assortment (
                                          ID INT AUTO_INCREMENT PRIMARY KEY,
                                          `name` VARCHAR(255),
                                          product_id INT NOT NULL,
                                          FOREIGN KEY (product_id) REFERENCES product(ID)
);

CREATE TABLE IF NOT EXISTS product_attribute (
                                                 ID INT AUTO_INCREMENT PRIMARY KEY,
                                                 `name` VARCHAR(255) NOT NULL,
                                                 `value` VARCHAR(255) NOT NULL,
                                                 product_id INT NOT NULL,
                                                 FOREIGN KEY (product_id) REFERENCES product(ID)
);

CREATE TABLE IF NOT EXISTS value (
                                     ID INT AUTO_INCREMENT PRIMARY KEY,
                                     `value` VARCHAR(255) NOT NULL,
                                     assortment_id INT NOT NULL,
                                     price INT,
                                     quantity INT,
                                     sold INT DEFAULT 0,
                                     FOREIGN KEY (assortment_id) REFERENCES assortment(ID)
);

CREATE TABLE IF NOT EXISTS coupon (
                                      ID INT AUTO_INCREMENT PRIMARY KEY,
                                      `code` VARCHAR(50) NOT NULL,
                                      `type` VARCHAR(50) NOT NULL,
                                      `value` INT NOT NULL,
                                      `status` VARCHAR(50) NOT NULL,
                                      coupon_per_user INT NOT NULL,
                                      coupon_uses INT NOT NULL,
                                      start_time TIMESTAMP NOT NULL,
                                      end_time TIMESTAMP NOT NULL,
                                      min_spend INT NOT NULL,
                                      max_spend INT NOT NULL
);

CREATE TABLE IF NOT EXISTS payment (
                                       ID INT AUTO_INCREMENT PRIMARY KEY,
                                       amount INT NOT NULL,
                                       payment_time TIMESTAMP NOT NULL,
                                       `status` ENUM('SUCCES', 'FAIL'),
                                       provider VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS `order` (
                                       ID INT AUTO_INCREMENT PRIMARY KEY,
                                       user_id INT NOT NULL,
                                       `status` ENUM('NULL','PENDING', 'CREATED','REJECTED') DEFAULT 'NULL',
                                       shipping_fee INT NOT NULL,
                                       total INT NOT NULL,
                                       payment_id INT NOT NULL,
                                       coupon_id INT NULL,
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       canceled_at TIMESTAMP NULL,
                                       completed_at TIMESTAMP NULL,
                                       delivery_at TIMESTAMP NULL,
                                       FOREIGN KEY (payment_id) REFERENCES payment(ID),
                                       FOREIGN KEY (coupon_id) REFERENCES coupon(ID),
                                       FOREIGN KEY (user_id) REFERENCES `user`(ID)
);
CREATE TABLE IF NOT EXISTS cart_item (
                                         ID INT AUTO_INCREMENT PRIMARY KEY,
                                         user_id INT NOT NULL,
                                         product_id INT NOT NULL,              
                                         quantity INT NOT NULL,
                                         price INT NOT NULL,
                                         slug VARCHAR(255) NOT NULL,
                                         FOREIGN KEY (user_id) REFERENCES `user`(ID),
                                         FOREIGN KEY (product_id) REFERENCES product(ID)                                    
);
CREATE TABLE IF NOT EXISTS order_item (
                                          ID INT AUTO_INCREMENT PRIMARY KEY,
                                          order_id INT NOT NULL,
                                          cart_item_id INT NOT NULL,
                                          FOREIGN KEY (order_id) REFERENCES `order`(ID),
                                          FOREIGN KEY (cart_item_id) REFERENCES `cart_item`(ID)
);

CREATE TABLE IF NOT EXISTS search_history (
                                              ID INT AUTO_INCREMENT PRIMARY KEY,
                                              user_id INT NOT NULL,
                                              search VARCHAR(255) NOT NULL,
                                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              FOREIGN KEY (user_id) REFERENCES user(ID)
);
CREATE TABLE IF NOT EXISTS otp
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    otp        VARCHAR(255)            NULL,
    created_at TIMESTAMP              NULL,
    expired_at TIMESTAMP              NULL,
    email      VARCHAR(255) UNIQUE KEY,
    CONSTRAINT pk_otp PRIMARY KEY (id)
);

CREATE TABLE permission
(
    name          VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_permission PRIMARY KEY (name)
);

CREATE TABLE `role`
(
    name          VARCHAR(255) NOT NULL,
    `description` VARCHAR(255) NULL,
    CONSTRAINT pk_role PRIMARY KEY (name)
);

CREATE TABLE role_permissions
(
    role_name        VARCHAR(255) NOT NULL,
    permissions_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_name, permissions_name)
);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permissions_name) REFERENCES permission (name);

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_name) REFERENCES `role` (name);

CREATE TABLE user_roles
(
    user_id    INT          NOT NULL,
    roles_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, roles_name)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_name) REFERENCES `role` (name);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES user (id);

CREATE TABLE token
(
    id         INT AUTO_INCREMENT NOT NULL,
    token      VARCHAR(100)       NOT NULL,
    user_id    INT                NULL,
    type       VARCHAR(255)       NULL,
    expired_at datetime           NULL,
    CONSTRAINT pk_token PRIMARY KEY (id)
);

ALTER TABLE token
    ADD CONSTRAINT uc_token_token UNIQUE (token);

ALTER TABLE token
    ADD CONSTRAINT FK_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);
    
-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.38 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.2.0.6576
-- --------------------------------------------------------
use ghtk_ecom;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table ghtk_ecom.address: ~2 rows (approximately)
INSERT INTO `address` (`id`, `name`, `type`, `address_parentID`, `created_user_id`) VALUES
	(3, 'Ha Noi', 'City', NULL, NULL),
	(4, 'Dong Da', 'District', 3, NULL),
    (5, 'Lang Ha', 'Ward', 4, NULL),
    (6, 'TRUNG LIET', 'Ward', 4, NULL),
    (7, 'Nam Tu Liem', 'District', 3, NULL);

-- Dumping data for table ghtk_ecom.assortment: ~1 rows (approximately)
INSERT INTO `assortment` (`ID`, `name`, `product_id`) VALUES
	(1, 'Bo nho', 1);

-- Dumping data for table ghtk_ecom.category: ~2 rows (approximately)
INSERT INTO `category` (`ID`, `name`, `description`, `parentID`, `image`, `updated_at`, `created_at`, `deleted_at`, `created_by_user`) VALUES
	(1, 'Phone', NULL, NULL, NULL, '2024-07-16 02:24:49', '2024-07-16 02:24:40', NULL, 1),
	(2, 'SmartPhone', NULL, 1, NULL, '2024-07-16 02:25:08', '2024-07-16 02:25:00', NULL, 1);

-- Dumping data for table ghtk_ecom.coupon: ~0 rows (approximately)

-- Dumping data for table ghtk_ecom.order: ~0 rows (approximately)

-- Dumping data for table ghtk_ecom.order_item: ~0 rows (approximately)

-- Dumping data for table ghtk_ecom.payment: ~0 rows (approximately)

-- Dumping data for table ghtk_ecom.product: ~1 rows (approximately)
INSERT INTO `product` (`ID`, `name`, `description`, `seller_id`, `category_id`, `slug`, `status`, `created_at`, `updated_at`, `deleted_at`, `price`, `quantity`, `sold`, `created_by_user`) VALUES
	(1, 'iPhone 11', NULL, 1, 2, NULL, 'show', '2024-07-16 02:25:37', '2024-07-16 02:25:37', NULL, 10, 10, 0, NULL);

-- Dumping data for table ghtk_ecom.product_asset: ~0 rows (approximately)

-- Dumping data for table ghtk_ecom.product_attribute: ~2 rows (approximately)
INSERT INTO `product_attribute` (`ID`, `name`, `value`, `product_id`) VALUES
	(1, 'Man hinh', 'IPS LCD 6.1" Liquid Retina', 1),
	(2, 'Chip', 'Apple A13', 1);

-- Dumping data for table ghtk_ecom.search_history: ~0 rows (approximately)

-- Dumping data for table ghtk_ecom.seller: ~0 rows (approximately)
INSERT INTO `seller` (`ID`, `user_id`, `name`, `rating`, `follow`, `address_id`, `address_detail`, `image_path`, `updated_at`, `created_at`, `deleted_at`) VALUES
	(1, 1, 'Hshop', 0, 1000, 6, 'So 1 ba dinh', NULL, '2024-07-16 02:23:32', '2024-07-16 02:23:22', NULL);

-- Dumping data for table ghtk_ecom.user: ~1 rows (approximately)
INSERT INTO `user` (`ID`, `name`, `email`, `password`, `first_name`, `last_name`, `phone_number`, `status`, `created_date_time`, `last_change_password_date_time`) VALUES
	(1, 'hieu123@gmail.com', 'hieu123@gmail.com', '$2a$10$It2cXpk3/NMYEH11uW9ZVuEEoIohPe8VrwjHUwfq.djHM1UXx7L3a', 'Nguyen', 'Hieu', '099999999', 'inactive', now(), now());

-- Dumping data for table ghtk_ecom.user_address: ~1 rows (approximately)
INSERT INTO `user_address` (`ID`, `name`, `phone_number`, `address_id`, `address_detail`, `user_id`, `is_default`) VALUES
	(1, 'Hieu', '012345678', 5, 'Lang Thuong', 1, true);

-- Dumping data for table ghtk_ecom.value: ~2 rows (approximately)
INSERT INTO `value` (`ID`, `value`, `assortment_id`, `price`, `quantity`, `sold`) VALUES
	(1, '64GB', 1, 8000, 100, 0),
	(2, '128GB', 1, 10000, 40, 0);
    
INSERT INTO `role` (name, description) VALUES
('USER', 'Regular user with limited permissions'),
('SELLER', 'Seller with permissions to manage products and sales'),
('ADMIN', 'Administrator with full access and control');

INSERT INTO `user_roles` (user_id, roles_name) values 
(1, 'USER'),
(1, 'SELLER');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
