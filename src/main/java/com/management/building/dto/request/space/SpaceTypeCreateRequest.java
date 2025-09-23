package com.management.building.dto.request.space;

import com.management.building.validators.NullOrRange;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceTypeCreateRequest {
  @NotBlank
  @Size(max = 150)
  String name;

  @NullOrRange(min = 0.0, nullable = false)
  Integer level;

  @Size(max = 1000)
  String description;

  @Size(max = 500)
  String specifications;

  @Min(value = 0)
  Integer maxCapacity;

  @Size(max = 150)
  String parentName;

  @Builder.Default
  Boolean requiresSpecialAccess = false;
}
