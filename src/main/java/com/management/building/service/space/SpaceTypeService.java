package com.management.building.service.space;

import java.util.List;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeTreeRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeTreeResponse;

public interface SpaceTypeService {
    SpaceTypeResponse create(SpaceTypeCreateRequest requestBody);

    List<SpaceTypeResponse> getAll();

    SpaceTypeResponse getByName(String name);

    SpaceTypeResponse update(String name, SpaceTypeUpdateRequest requestBody);

    SpaceTypeResponse updateParent(String name, String parentName);

    void delete(String name);

    SpaceTypeTreeResponse getSpaceTypeTree(String name, SpaceTypeTreeRequest requestBody);
}
