package com.management.building.dto.response.space;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.entity.space.Space;
import com.management.building.enums.space.SpaceStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceReponse {
  Long id;
  String name;
  SpaceStatus status;
  Integer capacity;
  Double area;
  Double length;
  Double width;
  Double height;
  Integer level;
  Space parentSpace;
  Set<Space> childSpaces;
  SpaceTypeResponse type;
  // fields to replace Lazy load in Space entity
  Long parentSpaceId;
  String spaceTypeName;
  Set<Long> childSpaceIds;
}
