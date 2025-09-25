package com.management.building.dto.request.smartDevice;

import jakarta.annotation.Nullable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
    @Nullable
    String description;
}
