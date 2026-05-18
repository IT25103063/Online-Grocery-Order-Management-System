# FreshLink Supplier Management Portal

A complete web application for managing grocery suppliers and their products.
Built with pure Java (no frameworks) and vanilla HTML/CSS/JavaScript.

## Prerequisites

- **Java JDK 11 or higher** installed and available in your system PATH
- A modern web browser (Chrome, Edge, Firefox)

## How to Run

**Double-click `run.bat`** in the project folder.

That's it. The script will:
1. Verify Java is installed
2. Compile all backend source files
3. Start the web server
4. Automatically open the application in your default browser

The server runs at `http://localhost:8080` (or the next available port if 8080 is busy).
Press `Ctrl+C` in the terminal to stop the server.

## Project Structure

```
Luxan Project Sample/
├── backend/
│   ├── src/                      # Java source code
│   │   ├── Main.java             # Entry point & server bootstrap
│   │   ├── StaticFileHandler.java # Serves HTML/CSS/JS files
│   │   ├── CORSHandler.java      # CORS header management
│   │   ├── JsonUtil.java         # JSON parsing & serialization
│   │   ├── Supplier.java         # Supplier data model
│   │   ├── SupplierFileStorage.java # Supplier file I/O
│   │   ├── SupplierHandler.java  # Supplier REST API
│   │   ├── Product.java          # Product data model
│   │   ├── ProductFileStorage.java # Product file I/O
│   │   └── ProductHandler.java   # Product REST API
│   └── bin/                      # Compiled .class files
├── data/
│   ├── suppliers.txt             # Supplier database (pipe-delimited)
│   ├── products.txt              # Product database (pipe-delimited)
│   └── backups/                  # Auto-created timestamped backups
├── frontend/
│   ├── index.html                # Dashboard
│   ├── supplier-list.html        # Supplier list with search & filters
│   ├── add-supplier.html         # Add new supplier form
│   ├── edit-supplier.html        # Edit existing supplier
│   └── view-supplier.html        # Supplier profile & products
├── run.bat                       # One-click launcher for Windows
└── README.md                     # This file
```

## Features

- **Dashboard** — Live statistics, charts, and recent supplier overview
- **Supplier CRUD** — Create, read, update, and delete suppliers
- **Product Management** — View and add products per supplier
- **Search & Filter** — Real-time search by name, category, or status
- **CSV Export** — Download supplier data as CSV
- **Auto Backups** — Database files are backed up on every server start
- **Dynamic Port** — Automatically finds a free port if 8080 is occupied

## API Endpoints

| Method | Endpoint                          | Description              |
|--------|-----------------------------------|--------------------------|
| GET    | `/api/suppliers`                  | List all suppliers       |
| GET    | `/api/suppliers?id=SUP-001`       | Get supplier by ID       |
| GET    | `/api/suppliers?search=farms`     | Search suppliers         |
| GET    | `/api/suppliers?status=active`    | Filter by status         |
| GET    | `/api/suppliers/stats`            | Dashboard statistics     |
| POST   | `/api/suppliers`                  | Create new supplier      |
| PUT    | `/api/suppliers?id=SUP-001`       | Update supplier          |
| DELETE | `/api/suppliers?id=SUP-001`       | Delete supplier          |
| GET    | `/api/products?supplierId=SUP-001`| Get products by supplier |
| POST   | `/api/products`                   | Add new product          |

## Data Storage

Data is stored in plain text files using pipe (`|`) delimiters:
- `data/suppliers.txt` — Supplier records
- `data/products.txt` — Product records
- `data/backups/` — Timestamped backup copies created on each server start
