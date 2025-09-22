package com.management.building.mapper.space;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.space.SpaceTypeHierarchyResponse;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeWithSpacesResponse;
import com.management.building.entity.space.SpaceType;

@Mapper(componentModel = "spring")
public interface SpaceTypeMapper {

    SpaceType toSpaceTypeFromCreateRequest(SpaceTypeCreateRequest fromEntity);

    SpaceTypeResponse toReponseFromSpaceType(SpaceType fromEntity);

    SpaceTypeWithSpacesResponse toResponseWithSpacesFromSpaceType(SpaceType fromEntity);

    SpaceTypeHierarchyResponse toReponseWithParentFromSpaceType(SpaceType fromEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSpaceTypeFromUpdateRequest(SpaceTypeUpdateRequest fromEntity,
            @MappingTarget SpaceType toEntity);

    default SpaceTypeHierarchyResponse toHierarchyResponseFromObject(Object[] obj) {
        return SpaceTypeHierarchyResponse.builder()
                .name((String) obj[0])
                .level((Integer) obj[1])
                .depth((Integer) obj[2])
                .path((String) obj[4])
                .build();
    }
}
