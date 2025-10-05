package com.management.building.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.device.DeviceStatus;

public interface DeviceStatusRepository extends JpaRepository<DeviceStatus, Long> {

}
