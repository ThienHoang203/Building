package com.management.building.dto.response.space;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.enums.space.SpaceStatus;

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
public class SpaceHierarchyResponse {
    Long id;
    String name;
    SpaceStatus status;
    Integer level;
    String path;
    Integer depth;
}