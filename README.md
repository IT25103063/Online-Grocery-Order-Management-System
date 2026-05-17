# FreshGrocer — Run Guide

## Requirements
- Java 17+
- Maven 3.8+

---

## How to Run

### Step 1 — Open a terminal in the project folder
```
cd FreshGrocerFixed
```

### Step 2 — Build and run
```
mvn spring-boot:run
```

### Step 3 — Open in browser
```
http://localhost:8080/user-management/login.html
```

---

## Where data is saved
All user data is saved to:
```
C:\Users\YourName\freshgrocer-data\users.txt   (Windows)
/home/yourname/freshgrocer-data/users.txt       (Mac/Linux)
```
This folder is created automatically when the app starts.

---

## Default Admin Login
| Field    | Value                    |
|----------|--------------------------|
| Email    | admin@freshgrocer.com    |
| Password | admin123                 |
| Role     | Admin (select the toggle)|

---

## Pages
| Page          | URL                                              |
|---------------|--------------------------------------------------|
| Login         | http://localhost:8080/user-management/login.html |
| Register      | http://localhost:8080/user-management/register.html |
| Forgot PW     | http://localhost:8080/user-management/forgot.html |
| Profile       | http://localhost:8080/user-management/profile.html |
