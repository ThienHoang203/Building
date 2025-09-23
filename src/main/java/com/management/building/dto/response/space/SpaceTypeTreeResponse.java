package com.management.building.dto.response.space;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

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
public class SpaceTypeTreeResponse {
    String name;
    String description;
    Integer level;
    String specifications;
    Boolean requiresSpecialAccess;
    Integer maxCapacity;
    Integer depth;
    @Builder.Default
    List<SpaceTypeTreeResponse> childrens = new ArrayList<>();
}
