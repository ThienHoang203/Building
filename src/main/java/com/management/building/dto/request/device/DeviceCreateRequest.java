package com.management.building.dto.request.device;

import jakarta.annotation.Nonnull;
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
public class DeviceCreateRequest {

    @Size(min = 3)
    @Nonnull
    String code;
    @Size(min = 2)
    @Nonnull
    String supplierCode;
    @Size(min = 2)
    @Nonnull
    String categoryCode;
    String name;
    String lat;
    String lon;
    Boolean isOnline;
    Long spaceId;

}
