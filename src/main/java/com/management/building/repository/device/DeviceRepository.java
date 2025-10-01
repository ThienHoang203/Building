package com.management.building.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.device.Device;

public interface DeviceRepository extends JpaRepository<Device, String> {
    boolean existsByName(String name);

    boolean existsByCode(String code);
}
