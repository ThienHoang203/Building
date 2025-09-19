package com.management.building.dto.response.space;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.enums.space.SpaceFunction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceTypeResponse {
  String name;
  String description;
  Integer level;
  String parentSpaceTypeId;
  Set<SpaceFunction> functions;
  String specifications;
  Boolean requiresSpecialAccess;
  Integer maxCapacity;
  Set<SpaceTypeResponse> childSpaceTypes;
  SpaceTypeResponse parentSpaceType;
  Set<SpaceReponse> spaces;
}
