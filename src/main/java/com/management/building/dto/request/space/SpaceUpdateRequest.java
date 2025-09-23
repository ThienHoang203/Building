package com.management.building.dto.request.space;

import com.management.building.enums.space.SpaceStatus;
import com.management.building.validators.NullOrRange;
import com.management.building.validators.ValidEnum;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceUpdateRequest {

    String name;

    @ValidEnum(enumClass = SpaceStatus.class, allowNull = true)
    SpaceStatus status;

    @NullOrRange(min = 0.0, nullable = true)
    Double area;

    @NullOrRange(min = 0.0, nullable = true)
    Integer capacity;
}
