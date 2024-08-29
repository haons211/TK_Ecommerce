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
