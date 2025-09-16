package com.management.building.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
