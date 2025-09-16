package com.management.building.mapper;

import org.mapstruct.Mapper;

import com.management.building.dto.response.SpaceReponse;
import com.management.building.entity.Space;

@Mapper(componentModel = "spring")
public interface SpaceMapper {
    SpaceReponse toSpaceResponseFromSpace(Space space);
}
