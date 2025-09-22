package com.management.building.dto.request.space;

import java.util.Set;

import com.management.building.enums.space.SpaceFunction;
import com.management.building.validators.ValidEnum;

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
public class SpaceTypeCreateRequest {
  @NotBlank
  @NotNull
  @Size(max = 150)
  private String name;

  @Size(max = 1000)
  private String description;

  @ValidEnum(enumClass = SpaceFunction.class)
  private Set<SpaceFunction> spaceFunctions;

  @Size(max = 500)
  private String specifications;

  @Min(value = 0)
  private Integer maxCapacity;

  @Size(max = 150)
  private String parentName;

  @Builder.Default
  private Boolean requiresSpecialAccess = false;
}
