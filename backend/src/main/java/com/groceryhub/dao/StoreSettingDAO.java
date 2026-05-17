package com.groceryhub.dao;

import com.groceryhub.model.StoreSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreSettingDAO extends JpaRepository<StoreSetting, Integer> {
    
    Optional<StoreSetting> findBySettingKey(String settingKey);
}
