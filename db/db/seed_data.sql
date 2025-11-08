-- seed_data.sql
USE restaurant;

-- Categories
INSERT INTO category (name) VALUES
('Soup'),
('Starter'),
('Main Course'),
('Punjabi'),
('Chinese'),
('South Indian'),
('Sizzlers'),
('Roti'),
('Drinks'),
('Ice Cream');

-- Menu items (category_id depends on order above)
-- Soups (category_id = 1)
INSERT INTO menu (category_id, name, price, description) VALUES
(1,'Tomato Soup',120,'Warm tomato soup with herbs'),
(1,'Sweet Corn Soup',130,'Corn & veg soup, veg stock'),
(1,'Manchow Soup',150,'Hot and spicy Manchow');

-- Starters (2)
INSERT INTO menu (category_id, name, price, description) VALUES
(2,'Paneer Tikka',250,'Tandoori paneer cubes with chutney'),
(2,'Veg Spring Roll',180,'Crispy rolls with veg filling');

-- Main Course (3)
INSERT INTO menu (category_id, name, price, description) VALUES
(3,'Veg Biryani',220,'Fragrant basmati with spices'),
(3,'Paneer Butter Masala',280,'Cottage cheese in creamy tomato gravy'),
(3,'Dal Makhani',200,'Slow-cooked black lentils');

-- Punjabi (4)
INSERT INTO menu (category_id, name, price, description) VALUES
(4,'Amritsari Kulcha',90,'Stuffed kulcha served with chole'),
(4,'Lassi',80,'Sweet or salted');

-- Chinese (5)
INSERT INTO menu (category_id, name, price, description) VALUES
(5,'Hakka Noodles',180,'Stir-fried noodles with veg'),
(5,'Chilli Paneer',220,'Spicy paneer Indo-Chinese');

-- South Indian (6)
INSERT INTO menu (category_id, name, price, description) VALUES
(6,'Masala Dosa',120,'Crispy dosa with potato masala'),
(6,'Idli Sambhar',100,'Soft idlis with sambhar and chutney');

-- Sizzlers (7)
INSERT INTO menu (category_id, name, price, description) VALUES
(7,'Paneer Sizzler',350,'Paneer with sauteed veggies served sizzling');

-- Roti (8)
INSERT INTO menu (category_id, name, price, description) VALUES
(8,'Butter Naan',50,'Soft buttered naan'),
(8,'Tandoori Roti',30,'Whole wheat tandoori roti');

-- Drinks (9)
INSERT INTO menu (category_id, name, price, description) VALUES
(9,'Cold Coffee',120,'Chilled coffee with cream'),
(9,'Fresh Lime Soda',100,'Sweet or salted');

-- Ice Cream (10)
INSERT INTO menu (category_id, name, price, description) VALUES
(10,'Vanilla Ice Cream',90,'Classic vanilla scoop'),
(10,'Chocolate Sundae',150,'Chocolate ice cream with toppings');

-- Create some restaurant tables
INSERT INTO restaurant_table (table_id, table_name, seats, occupied) VALUES
(1,'T1',4,FALSE),
(2,'T2',4,FALSE),
(3,'T3',6,FALSE),
(4,'T4',2,FALSE),
(5,'T5',6,FALSE);
