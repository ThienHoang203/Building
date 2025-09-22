package com.management.building.mapper.space;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpaceMapper {
    // SpaceReponse toSpaceResponseFromSpace(Space space);

    // Space toSpaceFromSpaceCreateRequest(SpaceCreateRequest request);

    // @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    // void updateSpaceFromSpaceUpdateRequest(SpaceUpdateRequest fromEntity, @MappingTarget Space toEntity);

    // @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    // default SpaceFlatResponse toSpaceFlatResponseFromObject(Object[] row) {
    //     return SpaceFlatResponse.builder()
    //             .id((Long) row[0])
    //             .name((String) row[1])
    //             .status(SpaceStatus.valueOf((String) row[2]))
    //             .capacity((Integer) row[3])
    //             .area((Double) row[4])
    //             .length((Double) row[5])
    //             .width((Double) row[6])
    //             .height((Double) row[7])
    //             .parentId((Long) row[8])
    //             .typeName((String) row[9])
    //             .level((Integer) row[10])
    //             .path((String) row[11])
    //             .build();
    // }
}
