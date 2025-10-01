package com.management.building.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.device.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);

    boolean existsByCodeAndName(String code, String name);

}
