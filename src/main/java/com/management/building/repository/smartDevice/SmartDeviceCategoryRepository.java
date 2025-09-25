package com.management.building.repository.smartDevice;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.smartDevice.SmartDeviceCategory;

public interface SmartDeviceCategoryRepository extends JpaRepository<SmartDeviceCategory, String> {

}
