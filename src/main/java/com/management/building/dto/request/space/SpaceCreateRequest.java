package com.management.building.dto.request.space;

import com.management.building.enums.space.SpaceStatus;
import com.management.building.validators.ValidEnum;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceCreateRequest {
    @NotBlank
    @NotNull
    @Size(max = 100)
    String name;

    @NotNull
    @ValidEnum(enumClass = SpaceStatus.class)
    SpaceStatus status;

    @DecimalMin(value = "0.0", inclusive = false)
    Double area;

    @Min(value = 0)
    Integer capacity;

    Long parentId;

    @NotBlank
    @NotNull
    @Size(max = 150)
    String typeName;
}