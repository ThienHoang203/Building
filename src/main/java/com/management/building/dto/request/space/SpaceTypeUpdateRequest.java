package com.management.building.dto.request.space;

import com.management.building.validators.NotBlankOrNull;
import com.management.building.validators.NullOrRange;

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
}
