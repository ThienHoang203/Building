package com.management.building.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.management.building.entity.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
