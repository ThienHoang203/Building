package com.management.building.dto.request;

import java.util.Set;

import com.management.building.enums.SpaceFunction;
import com.management.building.validators.ValidEnum;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceTypeUpdateRequest {

  @Size(max = 1000, message = "FIELD_SIZE_INVALID")
  @Nullable
  String description;

  @ValidEnum(enumClass = SpaceFunction.class, allowNull = true)
  Set<SpaceFunction> functions;

  @Size(max = 500, message = "FIELD_MAX_INVALID")
  @Nullable
  String specifications;

  @Nullable
  Boolean requiresSpecialAccess;

  @Min(value = 0, message = "FIELD_MIN_INVALID")
  @Max(value = Integer.MAX_VALUE, message = "FIELD_MAX_INVALID")
  @Nullable
  Integer maxCapacity;
}
