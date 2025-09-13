package com.management.building.service;

import org.springframework.stereotype.Service;

import com.management.building.repository.SpaceRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceService {
    SpaceRepository spaceRepo;

}
