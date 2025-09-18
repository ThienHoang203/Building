package com.management.building.dto.response;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.entity.Space;
import com.management.building.enums.SpaceFunction;

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
  Set<SpaceFunction> functions;
  String specifications;
  boolean requiresSpecialAccess;
  int maxCapacity;
  Set<Space> spaces;
}
