package com.management.building.dto.request.space;

import java.util.HashSet;
import java.util.Set;

import com.management.building.enums.space.SpaceFunction;
import com.management.building.validators.NotBlankOrNull;
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
public class SpaceTypeUpdateRequest {
    @NotBlankOrNull(nullable = true)
    String description;

    @NotBlankOrNull(nullable = true)
    String specifications;

    @NullOrRange(min = 0, nullable = true)
    Integer maxCapacity;

    Boolean requiresSpecialAccess;

    @ValidEnum(enumClass = SpaceFunction.class, allowNull = true, allowEmptySet = true)
    @Builder.Default
    Set<SpaceFunction> spaceFunctions = new HashSet<>();
}
