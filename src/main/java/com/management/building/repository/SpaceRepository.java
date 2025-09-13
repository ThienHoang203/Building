package com.management.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.Space;

public interface SpaceRepository extends JpaRepository<Space, String> {

}
