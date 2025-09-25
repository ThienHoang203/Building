package com.management.building.dto.request.smartDevice;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
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
public class SmartDeviceCreateRequest {
    @Size(min = 3)
    @Nonnull
    String code;

    @Size(min = 2)
    @Nonnull
    String name;

    @Size(min = 2)
    @Nonnull
    String categoryName;

    @Min(value = 1)
    @Nonnull
    Long supplierId;

    @Nullable
    Long spaceId;
}
