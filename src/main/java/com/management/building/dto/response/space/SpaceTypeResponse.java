package com.management.building.dto.response.space;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceTypeResponse {
  String name;
  String description;
  Integer level;
  String specifications;
  Boolean requiresSpecialAccess;
  Integer maxCapacity;
  String parentName;
}
