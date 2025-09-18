package com.management.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.SpaceType;

public interface SpaceTypeRepository extends JpaRepository<SpaceType, String> {
}
