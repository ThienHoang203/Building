package com.management.building.service.space;

import java.util.List;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceTreeRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceTreeResponse;

public interface SpaceService {
        SpaceResponse create(SpaceCreateRequest request);

        List<SpaceResponse> getAll();

        SpaceResponse getById(Long id);

        SpaceResponse update(Long id, SpaceUpdateRequest request);

        SpaceResponse updateParent(Long id, Long parentId);

        void delete(Long id);

        SpaceTreeResponse getSpaceTree(Long id, SpaceTreeRequest requestBody);
}
