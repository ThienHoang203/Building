package com.management.building.repository.smartDevice;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.smartDevice.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByCode(String code);
}
