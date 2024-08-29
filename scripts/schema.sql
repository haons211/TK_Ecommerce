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
