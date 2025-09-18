package com.management.building.dto.request;

import com.management.building.enums.SpaceStatus;
import com.management.building.validators.ValidEnum;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceCreateRequest {
    @NotBlank
    @NotNull
    String name;

    @NotNull
    String spaceTypeName;

    @Nullable
    Long parentSpaceId;

    @ValidEnum(enumClass = SpaceStatus.class, ignoreCase = true, allowNull = false)
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
