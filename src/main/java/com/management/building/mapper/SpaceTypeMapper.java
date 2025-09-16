package com.management.building.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.management.building.dto.request.SpaceTypeCreateRequest;
import com.management.building.dto.response.SpaceTypeResponse;
import com.management.building.entity.SpaceType;

@Mapper(componentModel = "spring")
public interface SpaceTypeMapper {
    @Mapping(target = "spaces", ignore = true)
    @Mapping(target = "requiresSpecialAccess", ignore = true)
    SpaceType toSpaceTypeFromSpaceTypeCreateRequest(SpaceTypeCreateRequest fromEntity);

    @Mapping(target = "spaces", ignore = true)
    SpaceTypeResponse toSpaceTypeReponseFromSpaceType(SpaceType fromEntity);
}
