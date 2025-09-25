package com.management.building.repository.smartDevice;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.smartDevice.SmartDevice;

public interface SmartDeviceRepository extends JpaRepository<SmartDevice, Long> {
    boolean existsByName(String name);

    boolean existsByCode(String code);
}
