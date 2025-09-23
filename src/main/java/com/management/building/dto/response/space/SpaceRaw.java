package com.management.building.dto.response.space;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.enums.space.SpaceStatus;

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
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceRaw {
    Long id;
    Double area;
    Integer capacity;
    String name;
    SpaceStatus status;
    Long parentId;
    String typeName;
    Integer level;
    Integer depth;
}
