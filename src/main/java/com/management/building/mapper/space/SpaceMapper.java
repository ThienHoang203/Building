package com.management.building.mapper.space;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceFlatResponse;
import com.management.building.dto.response.space.SpaceReponse;
import com.management.building.entity.space.Space;
import com.management.building.enums.space.SpaceStatus;

@Mapper(componentModel = "spring")
public interface SpaceMapper {
    @Mapping(target = "parentSpaceId", ignore = true)
    @Mapping(target = "childSpaceIds", ignore = true)
    @Mapping(target = "spaceTypeName", ignore = true)
    @Mapping(target = "parentSpace", ignore = true)
    @Mapping(target = "childSpaces", ignore = true)
    @Mapping(target = "type", ignore = true)
    SpaceReponse toSpaceResponseFromSpace(Space space);

    @Mapping(target = "parentSpace", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "childSpaces", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "level", source = "spaceTypeName")
    @Mapping(target = "typeName", source = "spaceTypeName")
    Space toSpaceFromSpaceCreateRequest(SpaceCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentSpace", ignore = true)
    @Mapping(target = "childSpaces", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "level", source = "spaceTypeName")
    @Mapping(target = "typeName", source = "spaceTypeName")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSpaceFromSpaceUpdateRequest(SpaceUpdateRequest fromEntity, @MappingTarget Space toEntity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    default SpaceFlatResponse toSpaceFlatResponseFromObject(Object[] row) {
        return SpaceFlatResponse.builder()
                .id((Long) row[0])
                .name((String) row[1])
                .status(SpaceStatus.valueOf((String) row[2]))
                .capacity((Integer) row[3])
                .area((Double) row[4])
                .length((Double) row[5])
                .width((Double) row[6])
                .height((Double) row[7])
                .parentId((Long) row[8])
                .typeName((String) row[9])
                .level((Integer) row[10])
                .path((String) row[11])
                .build();
    }
}
