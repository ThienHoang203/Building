package com.management.building.dto.response.space;

import java.util.ArrayList;
import java.util.List;

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
public class SpaceTreeResponse {
    Long id;
    Integer level;
    Integer depth;
    String name;
    SpaceStatus status;
    Integer capacity;
    Double area;
    String typeName;
    @Builder.Default
    List<SpaceTreeResponse> childrens = new ArrayList<>();
}
