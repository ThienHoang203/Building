package com.management.building.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.SpaceTypeCreateRequest;
import com.management.building.dto.request.SpaceTypeUpdateRequest;
import com.management.building.dto.response.SpaceTypeResponse;
import com.management.building.entity.SpaceType;

@Mapper(componentModel = "spring")
public interface SpaceTypeMapper {
    @Mapping(target = "spaces", ignore = true)
    @Mapping(target = "requiresSpecialAccess", ignore = true)
    SpaceType toSpaceTypeFromSpaceTypeCreateRequest(SpaceTypeCreateRequest fromEntity);

    @Mapping(target = "spaces", ignore = true)
    SpaceTypeResponse toSpaceTypeReponseFromSpaceType(SpaceType fromEntity);

    @Mapping(target = "spaces", ignore = true)
    @Mapping(target = "name", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSpaceTypeFromSpaceTypeUpdateRequest(SpaceTypeUpdateRequest fromEntity,
            @MappingTarget SpaceType toEntity);
}
