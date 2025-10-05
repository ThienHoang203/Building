package com.management.building.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.device.DeviceStatusLog;

public interface DeviceStatusLogRepository extends JpaRepository<DeviceStatusLog, Long> {

}
