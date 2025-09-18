package com.management.building.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.enums.SpaceStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaceFlatResponse {
    private Long id;
    private String name;
    private SpaceStatus status;
    private Integer capacity;
    private Double area;
    private Integer level;
    private String path;
    private Long parentId;
    private String typeName;
    private Double length;
    private Double width;
    private Double height;
}