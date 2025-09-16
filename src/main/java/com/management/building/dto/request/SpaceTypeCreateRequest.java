package com.management.building.dto.request;

import java.util.Set;

import com.management.building.enums.SpaceFunction;
import com.management.building.validators.ValidEnum;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NonNull
public class SpaceTypeCreateRequest {

    @NotBlank(message = "NOT_BLANK")
    String name;

    @Size(max = 1000, message = "FIELD_SIZE_INVALID")
    String description;

    @ValidEnum(enumClass = SpaceFunction.class)
    Set<SpaceFunction> functions;

    @Size(max = 500, message = "description is at least 500 characters long")
    String specifications;

    @Min(value = 0, message = "Maximum capacity must be non-negative")
    int maxCapacity;
}
