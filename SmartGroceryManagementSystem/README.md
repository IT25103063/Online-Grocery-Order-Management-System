# Smart Grocery Management System

A robust, enterprise-grade Java Web Application built for managing a modern grocery store. Developed using pure Java EE technologies without relying on an external database, utilizing advanced Object-Oriented Programming principles and an ultra-safe flat-file persistence system.

## 🚀 Key Features

* **Complete Role-Based Security:** Distinct portals for Customers, Standard Admins, and Super Admins.
* **OOP File Persistence:** Custom Data Access Objects (DAOs) using Encapsulation and file-rewriting mechanisms to prevent data corruption.
* **Polymorphic Data Models:** E.g., `Review` branches into `PublicReview` and `VerifiedReview`, automatically rendering different UI badges.
* **Real-time Order Cart:** JavaScript integration with Servlet backends for dynamic stock calculation.
* **Security Logging Engine:** Tracks Super Admin operations automatically.
* **Premium Bootstrap 5 UI:** Glassmorphism, Google Fonts, and micro-animations for an A+ presentation.

## 🛠️ Technology Stack

* **Backend:** Java 17, Jakarta Servlets 6.0, JSP (JavaServer Pages)
* **Frontend:** Bootstrap 5, JSTL (JavaServer Pages Standard Tag Library), JavaScript, HTML5/CSS3
* **Build Tool:** Apache Maven
* **Server:** Apache Tomcat 10+
* **Data Storage:** Flat Text Files (`.txt`) via `java.io`

---

## 🏃‍♂️ How to Run Locally

### 1. Prerequisites
Ensure you have the following installed on your machine:
* **Java Development Kit (JDK) 17** or higher.
* **Apache Maven** (for dependency resolution).
* **Apache Tomcat 10** (Jakarta EE 10 compatible).

### 2. File Path Configuration
The application stores its "database" in local text files. You **must** ensure the path in `Constants.java` points to a valid, writable directory on your computer.

1. Open `src/main/java/com/smartgrocery/util/Constants.java`.
2. Modify the `DATA_DIR` constant to point to the `data/` folder inside this project, or any absolute path on your C: drive.
   * *Example:* `public static final String DATA_DIR = "C:\\Users\\YOUR_NAME\\Documents\\SmartGroceryManagementSystem\\data\\";`
3. Ensure the folder exists. The system will automatically create the `.txt` files (`users.txt`, `products.txt`, etc.) if they do not exist.

### 3. Build the Project
Open your terminal in the root directory of the project (where `pom.xml` is located) and run:
```bash
mvn clean install
```
This will download the Jakarta dependencies and build the `.war` file.

### 4. Deploy to Tomcat
1. Copy the generated WAR file from the `target/` directory.
2. Paste it into your Tomcat `webapps/` directory.
3. Start the Tomcat server (`bin/startup.bat` on Windows).
4. Open your browser and navigate to: `http://localhost:8080/SmartGroceryManagementSystem/`

---

## 🔐 Default Credentials

To explore all the features during the presentation, use the following pre-configured accounts:

### Super Administrator
Has access to everything, including User Management, Supplier Management, and the Security Logs.
* **Username:** `admin_super`
* **Password:** `admin123`

### Customer
Can browse products, place orders, write reviews, and view their order history.
* **Username:** `john_customer`
* **Password:** `password123`

---
*Developed as a University Object-Oriented Programming Project.*
