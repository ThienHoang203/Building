package com.management.building.mapper.space;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.entity.space.SpaceType;

@Mapper(componentModel = "spring")
public interface SpaceTypeMapper {
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "spaces", ignore = true)
    @Mapping(target = "parentSpaceType", ignore = true)
    @Mapping(target = "childSpaceTypes", ignore = true)
    SpaceType toSpaceTypeFromSpaceTypeCreateRequest(SpaceTypeCreateRequest fromEntity);

    @Mapping(target = "spaces", ignore = true)
    @Mapping(target = "parentSpaceType", ignore = true)
    @Mapping(target = "childSpaceTypes", ignore = true)
    SpaceTypeResponse toSpaceTypeReponseFromSpaceType(SpaceType fromEntity);

    @Mapping(target = "spaces", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "parentSpaceType", ignore = true)
    @Mapping(target = "childSpaceTypes", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSpaceTypeFromSpaceTypeUpdateRequest(SpaceTypeUpdateRequest fromEntity,
            @MappingTarget SpaceType toEntity);

    default SpaceTypeResponse toSpaceTypeResponseFromObject(Object[] obj) {
        return SpaceTypeResponse.builder()
                .name((String) obj[0])
                .level((Integer) obj[1])
                .parentSpaceTypeId((String) obj[2])
                .maxCapacity((Integer) obj[3])
                .requiresSpecialAccess((Boolean) obj[4])
                .description((String) obj[5])
                .specifications((String) obj[6])
                .build();
    }
}
