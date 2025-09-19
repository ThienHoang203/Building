package com.management.building.service.space;

import java.util.List;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceFlatResponse;
import com.management.building.dto.response.space.SpacePaginationResponse;
import com.management.building.dto.response.space.SpaceReponse;
import com.management.building.dto.response.space.SpaceTreeResponse;

public interface SpaceService {
        public List<SpaceReponse> getAll();

        SpaceReponse getById(Long id);

        SpaceReponse create(SpaceCreateRequest requestBody);

        SpaceReponse update(Long id, SpaceUpdateRequest requestBody);

        void delete(Long id);

        SpacePaginationResponse<SpaceFlatResponse> getChildSpacesFlat(Long parentId, String cursor, Integer limit,
                        Integer maxDepth, String sortOrder);

        SpacePaginationResponse<SpaceTreeResponse> getChildSpacesNested(Long parentId, Integer maxDepth,
                        Boolean lazy, String sortOrder);

        SpacePaginationResponse<SpaceFlatResponse> getParentSpaces(Long spaceId, Integer maxDepth);

}
