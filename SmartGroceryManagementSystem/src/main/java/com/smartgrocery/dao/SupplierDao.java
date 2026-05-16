package com.smartgrocery.dao;

import com.smartgrocery.model.Supplier;
import com.smartgrocery.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupplierDao {

    public List<Supplier> findAll() {
        List<Supplier> suppliers = new ArrayList<>();
        File file = new File(Constants.SUPPLIERS_FILE);
        if (!file.exists()) return suppliers;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    suppliers.add(new Supplier(
                        parts[0], // id
                        parts[1], // companyName
                        parts[2], // contactName
                        parts[3], // phone
                        parts[4], // email
                        parts[5]  // address
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public Supplier findById(String id) {
        return findAll().stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean save(Supplier supplier) {
        if (supplier.getId() == null || supplier.getId().isEmpty()) {
            supplier.setId("SUP-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.SUPPLIERS_FILE, true))) {
            bw.write(formatLine(supplier));
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Supplier supplier) {
        List<Supplier> suppliers = findAll();
        boolean found = false;

        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i).getId().equals(supplier.getId())) {
                suppliers.set(i, supplier);
                found = true;
                break;
            }
        }

        if (found) {
            return rewriteFile(suppliers);
        }
        return false;
    }

    public boolean delete(String id) {
        List<Supplier> suppliers = findAll();
        boolean removed = suppliers.removeIf(s -> s.getId().equals(id));
        
        if (removed) {
            return rewriteFile(suppliers);
        }
        return false;
    }

    private boolean rewriteFile(List<Supplier> suppliers) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.SUPPLIERS_FILE, false))) {
            for (Supplier s : suppliers) {
                bw.write(formatLine(s));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatLine(Supplier s) {
        return s.getId() + "|" + 
               s.getCompanyName() + "|" + 
               s.getContactName() + "|" + 
               s.getPhone() + "|" + 
               s.getEmail() + "|" + 
               s.getAddress();
    }
}
