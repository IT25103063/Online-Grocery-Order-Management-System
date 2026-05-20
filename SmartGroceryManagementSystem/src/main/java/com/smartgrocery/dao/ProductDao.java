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

    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        File file = new File(Constants.PRODUCTS_FILE);
        if (!file.exists()) return products;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String id          = parts[0];
                    String name        = parts[1];
                    double price       = Double.parseDouble(parts[2]);
                    int stock          = Integer.parseInt(parts[3]);
                    String type        = parts[4];
                    String specialField = parts[5];
                    String category    = parts[6];

                    if ("PERISHABLE".equals(type)) {
                        products.add(new PerishableProduct(id, name, price, stock, specialField, category));
                    } else if ("NON_PERISHABLE".equals(type)) {
                        products.add(new NonPerishableProduct(id, name, price, stock, Integer.parseInt(specialField), category));
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return products;
    }

    /** Filter by category name. Pass null or empty to get all. */
    public List<Product> findByCategory(String category) {
        List<Product> all = findAll();
        if (category == null || category.isEmpty()) return all;
        List<Product> result = new ArrayList<>();
        for (Product p : all) {
            if (category.equalsIgnoreCase(p.getCategory())) result.add(p);
        }
        return result;
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
        return found && rewriteFile(products);
    }

    public boolean delete(String id) {
        List<Product> products = findAll();
        boolean removed = products.removeIf(p -> p.getId().equals(id));
        return removed && rewriteFile(products);
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
        String special = (p instanceof PerishableProduct)
                ? ((PerishableProduct) p).getExpirationDate()
                : String.valueOf(((NonPerishableProduct) p).getWarrantyMonths());
        return p.getId() + "|" + p.getName() + "|" + p.getPrice() + "|"
                + p.getStock() + "|" + p.getType() + "|" + special + "|" + p.getCategory();
    }
}