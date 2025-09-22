package com.management.building.dto.response.space;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.enums.space.SpaceFunction;

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
public class SpaceTypeWithSpacesResponse {
    String name;
    String description;
    Integer level;
    Set<SpaceFunction> spaceFunctions;
    String specifications;
    Boolean requiresSpecialAccess;
    Integer maxCapacity;
    List<SpaceResponse> spaces;
}
