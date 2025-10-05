package com.management.building.repository.device;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.building.entity.device.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
        boolean existsByName(String name);

        boolean existsByCodeAndName(String code, String name);

        @Query("SELECT c FROM Category c LEFT JOIN FETCH c.categoryStatus WHERE c.code IN :categoryCodes")
        Set<Category> findByCodeIn(@Param("categoryCodes") Set<String> categoryCodes);

}
