package com.management.building.dto.response.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.management.building.dto.response.space.SpaceResponse;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceWithRelationshipResponse {
    String deviceId;
    SpaceResponse space;
    CategoryResponse category;
    SupplierResponse supplier;
}
