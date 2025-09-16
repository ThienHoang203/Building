package com.management.building.dto.response;

import java.util.Set;

import com.management.building.entity.Space;
import com.management.building.entity.SpaceType;
import com.management.building.enums.SpaceStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceReponse {
    String id;

    String name;

    SpaceStatus status;

    int capacity;

    double area;

    Double length;

    Double width;

    Double height;

    Space parentSpace;

    Set<Space> childSpaces;

    SpaceType type;
}
