package com.management.building.dto.request.device;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceUpdateRequest {
    String name;
    String lat;
    String lon;
    Boolean isOnline;
    Long spaceId;
}
