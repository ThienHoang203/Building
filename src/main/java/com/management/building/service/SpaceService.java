package com.management.building.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.response.SpaceReponse;
import com.management.building.mapper.SpaceMapper;
import com.management.building.repository.SpaceRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceService {
    SpaceRepository spaceRepo;
    SpaceMapper spaceMapper;

    public List<SpaceReponse> getAll(boolean isAllLoaded) {
        var result = spaceRepo.findAll();

        return result.stream().map(spaceMapper::toSpaceResponseFromSpace).toList();
    }

    // public List<Object[]> getAllDescendants(String id) {
    // return spaceRepo.findAllDescendants(id);
    // }
}
