package com.management.building.dto.request.space;

import java.util.Set;

import com.management.building.enums.space.SpaceStatus;
import com.management.building.validators.ValidEnum;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceUpdateRequest {
    // @NotBlank
    @Nullable
    String name;

    // @NotBlank
    @Nullable
    String spaceTypeName;

    @Nullable
    Long parentSpaceId;

    @Nullable
    Set<Long> childSpaceIds;

    @ValidEnum(enumClass = SpaceStatus.class, ignoreCase = true, allowNull = true)
    SpaceStatus status;

    @Min(value = 0)
    @Nullable
    Integer capacity;

    @DecimalMin(value = "0.0", inclusive = false)
    @Nullable
    Double area;

    @DecimalMin(value = "0.0", inclusive = true)
    @Nullable
    Double length;

    @DecimalMin(value = "0.0", inclusive = true)
    @Nullable
    Double width;

    @DecimalMin(value = "0.0", inclusive = true)
    @Nullable
    Double height;
}
