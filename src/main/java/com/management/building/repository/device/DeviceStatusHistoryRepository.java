package com.management.building.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.device.DeviceStatusHistory;

public interface DeviceStatusHistoryRepository extends JpaRepository<DeviceStatusHistory, Long> {

}
