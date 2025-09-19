package com.management.building.dto.request.space;

import java.util.Set;

import com.management.building.enums.space.SpaceFunction;
import com.management.building.validators.ValidEnum;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
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

  @Min(value = 1)
  @Nullable
  String parentSpaceTypeId;

  @ValidEnum(enumClass = SpaceFunction.class, allowNull = true)
  Set<SpaceFunction> functions;

  @Size(max = 500, message = "FIELD_MAX_INVALID")
  String specifications;

  @Nullable
  Set<String> childSpaceTypeNames;

  @Min(value = 0, message = "FIELD_MIN_INVALID")
  @Max(value = Integer.MAX_VALUE, message = "FIELD_MAX_INVALID")
  Integer maxCapacity;

  @Builder.Default
  Boolean requiresSpecialAccess = false;
}
