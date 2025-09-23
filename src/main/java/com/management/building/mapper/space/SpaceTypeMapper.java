package com.management.building.mapper.space;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeRaw;
import com.management.building.dto.response.space.SpaceTypeTreeResponse;
import com.management.building.entity.space.SpaceType;

@Mapper(componentModel = "spring")
public interface SpaceTypeMapper {

    SpaceType toSpaceTypeFromCreateRequest(SpaceTypeCreateRequest request);

    @Mapping(target = "parentName", source = "parent.name")
    SpaceTypeResponse toReponseFromSpaceType(SpaceType entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSpaceTypeFromUpdateRequest(SpaceTypeUpdateRequest updateRequest,
            @MappingTarget SpaceType entity);

    @Mapping(target = "childrens", source = "children", qualifiedByName = "to_tree_list")
    SpaceTypeTreeResponse toTreeFromSpaceType(SpaceType entity);

    @Named("to_tree_list")
    default List<SpaceTypeTreeResponse> toTreeList(Set<SpaceType> spaceTypes) {
        if (spaceTypes == null) {
            return null;
        }
        return spaceTypes.stream()
                .map(this::toTreeFromSpaceType).toList();
    }

    SpaceTypeTreeResponse toTreeFromRaw(SpaceTypeRaw raw);

    default SpaceTypeRaw toTreeRawFromObject(Object[] obj) {
        return SpaceTypeRaw.builder()
                .name((String) obj[0])
                .level((Integer) obj[1])
                .maxCapacity((Integer) obj[2])
                .requiresSpecialAccess((Boolean) obj[3])
                .specifications((String) obj[4])
                .description((String) obj[5])
                .parentName((String) obj[6])
                .depth((Integer) obj[7])
                .build();
    }

    default SpaceTypeTreeResponse toTreeFromListRaw(String rootName, List<SpaceTypeRaw> raws) {
        Map<String, SpaceTypeTreeResponse> treeMap = new HashMap<>(raws.size());
        for (SpaceTypeRaw raw : raws) {
            treeMap.put(raw.getName(), toTreeFromRaw(raw));
        }
        var root = treeMap.get(rootName);
        for (SpaceTypeRaw raw : raws) {
            var parent = treeMap.getOrDefault(raw.getParentName(), null);
            if (parent == null)
                continue;
            var child = treeMap.get(raw.getName());
            parent.getChildrens().add(child);
        }
        return root;
    }
}
