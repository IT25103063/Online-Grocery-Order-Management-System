package com.smartgrocery.service;

import com.smartgrocery.dao.SupplierDao;
import com.smartgrocery.model.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierService {

    private SupplierDao supplierDao;

    public SupplierService() {
        this.supplierDao = new SupplierDao();
    }

    public List<Supplier> getAllSuppliers() {
        return supplierDao.findAll();
    }

    public Supplier getSupplierById(String id) {
        return supplierDao.findById(id);
    }

    public List<Supplier> searchSuppliers(String keyword) {
        List<Supplier> all = getAllSuppliers();
        if (keyword == null || keyword.trim().isEmpty()) return all;

        String lower = keyword.toLowerCase();
        List<Supplier> filtered = new ArrayList<>();

        for (Supplier s : all) {
            if (s.getCompanyName().toLowerCase().contains(lower) ||
                s.getContactName().toLowerCase().contains(lower) ||
                s.getEmail().toLowerCase().contains(lower)) {
                filtered.add(s);
            }
        }
        return filtered;
    }

    public String saveOrUpdate(Supplier supplier) {
        // Basic Validation
        if (supplier.getCompanyName() == null || supplier.getCompanyName().trim().isEmpty()) {
            return "Company Name is required.";
        }
        if (supplier.getEmail() == null || !supplier.getEmail().contains("@")) {
            return "A valid Email Address is required.";
        }

        boolean success;
        if (supplier.getId() == null || supplier.getId().isEmpty()) {
            success = supplierDao.save(supplier);
        } else {
            success = supplierDao.update(supplier);
        }

        return success ? "SUCCESS" : "File error occurred while saving supplier.";
    }

    public boolean deleteSupplier(String id) {
        return supplierDao.delete(id);
    }
}
