package com.management.building.dto.request.tuyaCloud.device;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceInProjectRequest {
    String productIds;
    String categories;
    String last_id;
    Integer page_size;
}
