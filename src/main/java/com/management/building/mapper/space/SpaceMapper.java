package com.management.building.mapper.space;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceHierarchyResponse;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceWithChildrenResponse;
import com.management.building.entity.space.Space;
import com.management.building.enums.space.SpaceStatus;

@Mapper(componentModel = "spring")
public interface SpaceMapper {

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "id", ignore = true)
    Space toSpaceFromCreateRequest(SpaceCreateRequest request);

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    @Mapping(source = "type.name", target = "typeName")
    @Mapping(source = "type.description", target = "typeDescription")
    SpaceResponse toResponseFromSpace(Space entity);

    @Mapping(source = "type.name", target = "typeName")
    SpaceWithChildrenResponse toResponseWithChildrenFromSpace(Space entity);

    SpaceHierarchyResponse toHierarchyResponseFromSpace(Space entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateSpaceFromUpdateRequest(SpaceUpdateRequest request, @MappingTarget Space entity);

    default SpaceHierarchyResponse toHierarchyResponseFromObject(Object[] obj) {
        return SpaceHierarchyResponse.builder()
                .id((Long) obj[0])
                .name((String) obj[1])
                .status(SpaceStatus.valueOf((String) obj[2]))
                .level((Integer) obj[3])
                .depth((Integer) obj[4])
                .path((String) obj[5])
                .build();
    }
}
