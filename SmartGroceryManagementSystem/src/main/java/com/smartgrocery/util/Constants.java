package com.smartgrocery.util;

import java.io.File;

public class Constants {
    // Keep your original working path
    public static final String DATA_DIR = "C:" + File.separator + "Users" + File.separator + "LENOVO" + File.separator + "Documents" + File.separator + "y1s2ooppro" + File.separator + "SmartGroceryManagementSystem" + File.separator + "data" + File.separator;

    public static final String USERS_FILE = DATA_DIR + "users.txt";
    public static final String ADMINS_FILE = DATA_DIR + "admins.txt";
    public static final String PRODUCTS_FILE = DATA_DIR + "products.txt";
    public static final String ORDERS_FILE = DATA_DIR + "orders.txt";
    public static final String ADMIN_LOGS_FILE = DATA_DIR + "admin_logs.txt";
    public static final String SUPPLIERS_FILE = DATA_DIR + "suppliers.txt";
    public static final String REVIEWS_FILE = DATA_DIR + "reviews.txt";

    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}