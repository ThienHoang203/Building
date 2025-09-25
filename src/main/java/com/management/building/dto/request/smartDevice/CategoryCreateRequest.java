package com.management.building.dto.request.smartDevice;

import jakarta.annotation.Nullable;
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
    @Size(max = 100, min = 2)
    String name;

    @Nullable
    String description;
}
