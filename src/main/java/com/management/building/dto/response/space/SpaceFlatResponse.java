package com.management.building.dto.response.space;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.enums.space.SpaceStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpaceFlatResponse {
    Long id;
    String name;
    SpaceStatus status;
    Integer capacity;
    Double area;
    Integer level;
    String path;
    Long parentId;
    String typeName;
    Double length;
    Double width;
    Double height;
}