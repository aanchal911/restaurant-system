-- rst_schema.sql
CREATE DATABASE IF NOT EXISTS restaurant;
USE restaurant;

-- categories table
CREATE TABLE IF NOT EXISTS category (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

-- menu items, linked to category
CREATE TABLE IF NOT EXISTS menu (
  item_id INT AUTO_INCREMENT PRIMARY KEY,
  category_id INT NOT NULL,
  name VARCHAR(150) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  description TEXT,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- restaurant tables (physical)
CREATE TABLE IF NOT EXISTS restaurant_table (
  table_id INT PRIMARY KEY,
  table_name VARCHAR(50),
  seats INT DEFAULT 4,
  occupied BOOLEAN DEFAULT FALSE
);

-- orders (simple parent)
CREATE TABLE IF NOT EXISTS orders (
  order_id INT AUTO_INCREMENT PRIMARY KEY,
  table_id INT,
  is_closed BOOLEAN DEFAULT FALSE,
  opened_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  closed_at TIMESTAMP NULL,
  FOREIGN KEY (table_id) REFERENCES restaurant_table(table_id)
);

-- order items (child)
CREATE TABLE IF NOT EXISTS order_items (
  order_item_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  item_id INT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  unit_price DECIMAL(10,2) NOT NULL,
  added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES orders(order_id),
  FOREIGN KEY (item_id) REFERENCES menu(item_id)
);
