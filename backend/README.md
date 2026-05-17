# Online Grocery Order Management System - Backend

## Overview
This is a comprehensive Java Spring Boot backend for the Online Grocery Order Management System. It provides RESTful APIs for managing admins, customers, products, categories, orders, inventory, deliveries, and more.

## Tech Stack
- **Java 17+**
- **Spring Boot 3.2.5** (Web, Data JPA, Security, Validation)
- **MySQL 8.0+**
- **JWT (JSON Web Token)** for Authentication
- **Lombok**
- **HikariCP** (Connection Pooling)

## Database Setup
1. Create a MySQL database (or let Spring Boot create it automatically via the JDBC URL if the user has rights).
2. The schema (`schema.sql`) and seed data (`seed.sql`) will automatically be executed on application startup based on the `spring.sql.init.mode=always` property in `application.properties`.
3. Update `application.properties` with your MySQL root credentials:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

## Default Admins
The system will seed two default admin accounts:
- **Super Admin:** `admin@grocery.com` / `admin123`
- **Store Manager:** `manager@grocery.com` / `manager123`

## Running the Application
Ensure you have Maven installed.

```bash
# Clean and package
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## Implemented So Far
- Complete Entity/Model layer for all 13 tables
- Complete Repository/DAO layer with native SQL queries and JPQL
- Authentication & JWT Security layer
- Dashboard & Analytics features
- Products CRUD
- Standard Exception Handling & Generic API Responses

*Further controllers (Orders, Customers, Settings, Promotions) follow the exact same established architecture pattern.*
