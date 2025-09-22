package com.management.building.service.space;

import java.util.List;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateWithSpacesRequest;
import com.management.building.dto.response.space.SpaceTypeHierarchyResponse;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeWithSpacesResponse;
import com.management.building.enums.space.LoadingHierarchyMode;
import com.management.building.enums.space.UpdateListSpacesMode;
import com.management.building.enums.space.UpdateParentMode;

import jakarta.validation.constraints.NotBlank;

public interface SpaceTypeService {
    SpaceTypeResponse create(SpaceTypeCreateRequest requestBody);

    List<SpaceTypeResponse> getAll();

    SpaceTypeResponse getByName(String name);

    SpaceTypeResponse update(String name, SpaceTypeUpdateRequest requestBody);

    SpaceTypeResponse updateParent(String name, String parentName, UpdateParentMode mode);

    void delete(String name);

    List<SpaceTypeHierarchyResponse> getParents(String name, LoadingHierarchyMode mode);

    List<SpaceTypeHierarchyResponse> getChildren(String name);

    SpaceTypeWithSpacesResponse getByNameWithSpaces(String name);

    SpaceTypeWithSpacesResponse updateWithSpaces(String name, UpdateListSpacesMode updateMode,
            SpaceTypeUpdateWithSpacesRequest requestBody);
}
