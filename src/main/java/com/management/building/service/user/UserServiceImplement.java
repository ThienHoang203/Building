package com.management.building.service.user;

import org.springframework.stereotype.Service;

import com.management.building.repository.user.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImplement implements UserService {
    UserRepository userRepo;

}
