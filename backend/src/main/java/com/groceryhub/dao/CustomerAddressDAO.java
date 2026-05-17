package com.groceryhub.dao;

import com.groceryhub.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAddressDAO extends JpaRepository<CustomerAddress, Integer> {
    
    List<CustomerAddress> findByCustomer_CustomerId(Integer customerId);
    
    List<CustomerAddress> findByCustomer_CustomerIdAndIsDefaultTrue(Integer customerId);
}
