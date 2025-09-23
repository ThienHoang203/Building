package com.management.building.mapper.space;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceRaw;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceTreeResponse;
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
    @Mapping(source = "type.name", target = "typeName")
    @Mapping(source = "type.level", target = "level")
    SpaceResponse toResponseFromSpace(Space entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSpaceFromUpdateRequest(SpaceUpdateRequest request, @MappingTarget Space entity);

    SpaceTreeResponse toTreeFromRaw(SpaceRaw raw);

    default SpaceRaw toTreeRawFromObject(Object[] obj) {
        return SpaceRaw.builder()
                .id((Long) obj[0])
                .area((Double) obj[1])
                .capacity((Integer) obj[2])
                .name((String) obj[3])
                .status(obj[4] != null ? SpaceStatus.valueOf((String) obj[4]) : null)
                .parentId(obj[0] != null ? (Long) obj[5] : null)
                .typeName((String) obj[6])
                .depth((Integer) obj[7])
                .level((Integer) obj[8])
                .build();
    }

    default SpaceTreeResponse toTreeFromListRaw(Long rootId, List<SpaceRaw> raws) {
        Map<Long, SpaceTreeResponse> treeMap = new HashMap<>(raws.size());
        for (SpaceRaw raw : raws) {
            treeMap.put(raw.getId(), toTreeFromRaw(raw));
        }
        var root = treeMap.get(rootId);
        for (SpaceRaw raw : raws) {
            var parent = treeMap.getOrDefault(raw.getParentId(), null);
            if (parent == null)
                continue;
            var child = treeMap.get(raw.getId());
            parent.getChildrens().add(child);
        }
        return root;
    }
}
