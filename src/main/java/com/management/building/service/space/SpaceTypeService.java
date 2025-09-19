package com.management.building.service.space;

import java.util.List;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.space.SpaceTypeResponse;

public interface SpaceTypeService {
    SpaceTypeResponse create(SpaceTypeCreateRequest requestBody);

    List<SpaceTypeResponse> getSpaceTypes();

    SpaceTypeResponse getSpaceTypeByName(String name);

    SpaceTypeResponse update(String name, SpaceTypeUpdateRequest requestBody);

    void delete(String name);

    boolean existsByName(String name);
}
