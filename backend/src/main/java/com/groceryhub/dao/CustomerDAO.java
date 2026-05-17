package com.groceryhub.dao;

import com.groceryhub.model.Customer;
import com.groceryhub.util.TextFileDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CustomerDAO {

    private final TextFileDatabase db;
    private final String FILE_NAME = "customers";

    public List<Customer> findAll() {
        return db.loadData(FILE_NAME, Customer.class);
    }
    
    public long count() {
        return findAll().size();
    }

    public Optional<Customer> findById(Integer id) {
        return findAll().stream().filter(c -> c.getCustomerId().equals(id)).findFirst();
    }

    public Optional<Customer> findByEmail(String email) {
        return findAll().stream().filter(c -> c.getEmail().equals(email)).findFirst();
    }

    public boolean existsByEmail(String email) {
        return findAll().stream().anyMatch(c -> c.getEmail().equals(email));
    }

    public boolean existsByPhone(String phone) {
        return findAll().stream().anyMatch(c -> c.getPhone().equals(phone));
    }

    public Page<Customer> searchCustomers(String search, Pageable pageable) {
        List<Customer> all = findAll().stream()
            .filter(c -> search == null || c.getFullName().contains(search) || c.getEmail().contains(search) || c.getPhone().contains(search))
            .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), all.size());
        if (start > all.size()) return new PageImpl<>(List.of(), pageable, all.size());
        return new PageImpl<>(all.subList(start, end), pageable, all.size());
    }

    public Customer save(Customer customer) {
        List<Customer> customers = findAll();
        if (customer.getCustomerId() == null) {
            customer.setCustomerId(customers.size() > 0 ? customers.stream().mapToInt(Customer::getCustomerId).max().orElse(0) + 1 : 1);
            customers.add(customer);
        } else {
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getCustomerId().equals(customer.getCustomerId())) {
                    customers.set(i, customer);
                    break;
                }
            }
        }
        db.saveData(FILE_NAME, customers);
        return customer;
    }
}
