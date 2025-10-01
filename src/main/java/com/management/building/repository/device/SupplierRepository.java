package com.management.building.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.device.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, String> {
    boolean existsByName(String name);

    boolean existsByCodeAndName(String code, String name);
}
