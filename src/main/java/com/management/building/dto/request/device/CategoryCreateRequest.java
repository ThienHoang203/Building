package com.management.building.dto.request.device;

import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryCreateRequest {
    @Size(max = 20, min = 2)
    String code;

    @Size(max = 100, min = 3)
    String name;
}
