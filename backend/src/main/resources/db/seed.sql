-- =============================================
-- Grocery Admin Portal - Seed Data
-- =============================================
USE grocery_admin_db;

-- Admin Users (password: admin123 -> BCrypt hash)
INSERT IGNORE INTO admins (full_name, email, password_hash, role, phone) VALUES
('Admin User', 'admin@grocery.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZQQp7E0CzGJOqHYk1NOBokTFnjXu', 'super_admin', '+91 99999 00000'),
('Store Manager', 'manager@grocery.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjZQQp7E0CzGJOqHYk1NOBokTFnjXu', 'manager', '+91 88888 00000');

-- Categories
INSERT IGNORE INTO categories (name, description, image_url) VALUES
('Fruits', 'Fresh seasonal fruits', '🍎'),
('Vegetables', 'Farm fresh vegetables', '🥦'),
('Dairy', 'Milk, cheese, and dairy products', '🧀'),
('Bakery', 'Fresh bread and baked goods', '🍞'),
('Beverages', 'Drinks and juices', '🥤'),
('Snacks', 'Chips, cookies and snacks', '🍪');

-- Store Settings
INSERT IGNORE INTO store_settings (setting_key, setting_value, description) VALUES
('store_name', 'FreshMart Grocery', 'Store display name'),
('store_address', '123 Green Street, Mumbai', 'Store physical address'),
('store_phone', '+91 98765 43210', 'Store contact phone'),
('store_email', 'contact@freshmart.com', 'Store contact email'),
('zone1_charge', '30', 'Delivery charge for Zone 1'),
('zone2_charge', '50', 'Delivery charge for Zone 2'),
('free_delivery_min', '500', 'Minimum order for free delivery'),
('delivery_slots', '9AM-12PM, 12PM-3PM, 3PM-6PM, 6PM-9PM', 'Available delivery time slots');
