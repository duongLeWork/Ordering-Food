# Thêm cơ sở dữ liệu để nghịch

Ở đây cá nhân mình sử dụng **XAMPP** với **phpMyAdmin** nên trong `application.properties` mình đã để mật khẩu của root trống, bạn nếu dùng đồ khác thì tự thêm nha. Dưới đây là phần thêm các Entity tương ứng với sơ đồ ER của anh Dương. 

### Đồ chơi chính

```sql
-- Tạo database
CREATE DATABASE IF NOT EXISTS foodapp;
USE foodapp;

-- Tạo bảng Account
CREATE TABLE Account (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Tạo bảng Customer
CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15) UNIQUE,
    account_id INT UNIQUE,
    FOREIGN KEY (account_id) REFERENCES Account(account_id) ON DELETE CASCADE
);

-- Tạo bảng Manager
CREATE TABLE Manager (
    manager_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    account_id INT UNIQUE,
    FOREIGN KEY (account_id) REFERENCES Account(account_id) ON DELETE CASCADE
);

-- Tạo bảng Address
CREATE TABLE Address (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    street_number VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL
);

-- Tạo bảng CustomerAddress
CREATE TABLE CustomerAddress (
    customer_address_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    address_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES Address(address_id) ON DELETE CASCADE
);

-- Tạo bảng Restaurant
CREATE TABLE Restaurant (
    restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
    restaurant_name VARCHAR(100) NOT NULL,
    address_id INT UNIQUE,
    manager_id INT UNIQUE,
    FOREIGN KEY (address_id) REFERENCES Address(address_id) ON DELETE CASCADE,
    FOREIGN KEY (manager_id) REFERENCES Manager(manager_id) ON DELETE CASCADE
);

-- Tạo bảng Food
CREATE TABLE Food (
    food_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT,
    image VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE
);

-- Tạo bảng FoodOrder
CREATE TABLE FoodOrder (
    food_order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_status_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- Tạo bảng OrderStatus
CREATE TABLE OrderStatus (
    order_status_id INT AUTO_INCREMENT PRIMARY KEY,
    status_value VARCHAR(50) NOT NULL
);

-- Tạo bảng OrderMenuItem (Chi tiết món ăn trong đơn hàng)
CREATE TABLE OrderMenuItem (
    order_menu_id INT AUTO_INCREMENT PRIMARY KEY,
    food_order_id INT,
    food_id INT,
    quantity_ordered INT NOT NULL,
    FOREIGN KEY (food_order_id) REFERENCES FoodOrder(food_order_id) ON DELETE CASCADE,
    FOREIGN KEY (food_id) REFERENCES Food(food_id) ON DELETE CASCADE
);

```

### Thêm các dữ liệu mẫu

```sql
use foodapp;

INSERT INTO Account (username, password, email, role) VALUES
('customer1', 'password123', 'customer1@example.com', 'CUSTOMER'),
('customer2', 'password123', 'customer2@example.com', 'CUSTOMER'),
('manager1', 'password123', 'manager1@example.com', 'MANAGER'),
('manager2', 'password123', 'manager2@example.com', 'MANAGER');
INSERT INTO Customer (first_name, last_name, phone_number, account_id) VALUES
('John', 'Doe', '1234567890', 1),
('Alice', 'Smith', '0987654321', 2);
INSERT INTO Manager (first_name, last_name, account_id) VALUES
('Robert', 'Brown', 3),
('Emily', 'Johnson', 4);
INSERT INTO Address (street_number, city, country) VALUES
('123 Main St', 'New York', 'USA'),
('456 Elm St', 'Los Angeles', 'USA'),
('789 Oak St', 'Chicago', 'USA');
INSERT INTO CustomerAddress (customer_id, address_id) VALUES
(1, 1),
(2, 2);
INSERT INTO Restaurant (restaurant_name, address_id, manager_id) VALUES
('Pizza Paradise', 1, 1),
('Burger Heaven', 2, 2);
INSERT INTO Food (name, price, description, image, is_available) VALUES
('Pepperoni Pizza', 12.99, 'A delicious pizza with pepperoni toppings', 'pepperoni.jpg', TRUE),
('Cheeseburger', 9.99, 'Classic cheeseburger with fries', 'burger.jpg', TRUE),
('Veggie Pizza', 11.99, 'Healthy pizza with fresh vegetables', 'veggie.jpg', TRUE),
('Grilled Chicken', 14.99, 'Grilled chicken with special sauce', 'chicken.jpg', TRUE);
INSERT INTO OrderStatus (status_value) VALUES
('Pending'),
('Processing'),
('Completed'),
('Cancelled');
INSERT INTO FoodOrder (customer_id, order_status_id, total_amount) VALUES
(1, 1, 22.98),  -- Order by John Doe
(2, 2, 9.99);   -- Order by Alice Smith
INSERT INTO OrderMenuItem (food_order_id, food_id, quantity_ordered) VALUES
(1, 1, 1),  -- 1x Pepperoni Pizza for John Doe
(1, 3, 1),  -- 1x Veggie Pizza for John Doe
(2, 2, 1);  -- 1x Cheeseburger for Alice Smith



```