package com.management.building.repository.device;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.device.CategoryStatus;

public interface CategoryStatusRepository extends JpaRepository<CategoryStatus, String> {

}
