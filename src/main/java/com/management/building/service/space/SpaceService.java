package com.management.building.service.space;

import java.util.List;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceHierarchyResponse;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceWithChildrenResponse;
import com.management.building.enums.space.LoadingHierarchyMode;
import com.management.building.enums.space.SpaceStatus;
import com.management.building.enums.space.UpdateParentMode;

public interface SpaceService {
        SpaceResponse create(SpaceCreateRequest request);

        List<SpaceResponse> getAll();

        SpaceResponse getById(Long id);

        List<SpaceResponse> getByStatus(SpaceStatus status);

        List<SpaceResponse> getByTypeName(String typeName);

        SpaceResponse update(Long id, SpaceUpdateRequest request);

        SpaceResponse updateStatus(Long id, SpaceStatus status);

        SpaceResponse updateParent(Long id, Long parentId, UpdateParentMode mode);

        void delete(Long id);

        List<SpaceHierarchyResponse> getParents(Long id, LoadingHierarchyMode mode);

        SpaceWithChildrenResponse getChildren(Long id);

        List<SpaceResponse> getRootSpaces();
}
