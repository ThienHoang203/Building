package com.management.building.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.SpaceTypeCreateRequest;
import com.management.building.dto.response.SpaceTypeResponse;
import com.management.building.mapper.SpaceTypeMapper;
import com.management.building.repository.SpaceTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceTypeService {
    SpaceTypeRepository spaceTypeRepo;
    SpaceTypeMapper spaceTypeMapper;

    public SpaceTypeResponse create(SpaceTypeCreateRequest request) {
        var spaceType = spaceTypeMapper.toSpaceTypeFromSpaceTypeCreateRequest(request);

        var saveResult = spaceTypeRepo.save(spaceType);

        var responseData = spaceTypeMapper.toSpaceTypeReponseFromSpaceType(saveResult);

        return responseData;
    }

    public List<SpaceTypeResponse> getSpaceTypes() {
        var result = spaceTypeRepo.findAll();

        var reponseData = result.stream().map(spaceTypeMapper::toSpaceTypeReponseFromSpaceType).toList();

        return reponseData;
    }

    public boolean existsByName(String name) {
        return spaceTypeRepo.existsById(name);
    }
}
