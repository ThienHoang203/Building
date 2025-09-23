package com.management.building.dto.request.space;

import com.management.building.enums.space.SpaceStatus;
import com.management.building.validators.NullOrRange;
import com.management.building.validators.ValidEnum;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
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
    @Size(max = 100)
    @NotBlank
    String name;

    @ValidEnum(enumClass = SpaceStatus.class, allowNull = false)
    SpaceStatus status;

    @NullOrRange(min = 0.0, nullable = true)
    Double area;

    @NullOrRange(min = 0.0, nullable = true)
    Integer capacity;

    @DecimalMin(value = "1.0", inclusive = true, message = "dfa")
    Long parentId;

    @NotBlank
    String typeName;
}