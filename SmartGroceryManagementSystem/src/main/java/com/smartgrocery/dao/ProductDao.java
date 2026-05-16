package com.smartgrocery.dao;

import com.smartgrocery.model.NonPerishableProduct;
import com.smartgrocery.model.PerishableProduct;
import com.smartgrocery.model.Product;
import com.smartgrocery.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductDao {

    /**
     * Reads all products from the text file. Demonstrates Polymorphism.
     */
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        File file = new File(Constants.PRODUCTS_FILE);
        if (!file.exists()) return products;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int stock = Integer.parseInt(parts[3]);
                    String type = parts[4];
                    String specialField = parts[5];

                    if ("PERISHABLE".equals(type)) {
                        products.add(new PerishableProduct(id, name, price, stock, specialField));
                    } else if ("NON_PERISHABLE".equals(type)) {
                        products.add(new NonPerishableProduct(id, name, price, stock, Integer.parseInt(specialField)));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product findById(String id) {
        return findAll().stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean save(Product product) {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.PRODUCTS_FILE, true))) {
            bw.write(formatLine(product));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Product product) {
        List<Product> products = findAll();
        boolean found = false;
        
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                found = true;
                break;
            }
        }

        if (found) {
            return rewriteFile(products);
        }
        return false;
    }

    public boolean delete(String id) {
        List<Product> products = findAll();
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        
        if (removed) {
            return rewriteFile(products);
        }
        return false;
    }

    private boolean rewriteFile(List<Product> products) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.PRODUCTS_FILE, false))) {
            for (Product p : products) {
                bw.write(formatLine(p));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatLine(Product p) {
        if (p instanceof PerishableProduct) {
            return p.getId() + "|" + p.getName() + "|" + p.getPrice() + "|" + p.getStock() + "|" + p.getType() + "|" + ((PerishableProduct) p).getExpirationDate();
        } else {
            return p.getId() + "|" + p.getName() + "|" + p.getPrice() + "|" + p.getStock() + "|" + p.getType() + "|" + ((NonPerishableProduct) p).getWarrantyMonths();
        }
    }
}
