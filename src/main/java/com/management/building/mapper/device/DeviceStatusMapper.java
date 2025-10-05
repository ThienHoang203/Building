package com.management.building.mapper.device;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.management.building.dto.request.device.DeviceStatusCreateRequest;
import com.management.building.dto.response.device.DeviceStatusResponse;
import com.management.building.entity.device.DeviceStatus;

@Mapper(componentModel = "spring")
public interface DeviceStatusMapper {
    DeviceStatus toStatusFromCreateRequest(DeviceStatusCreateRequest createRequest);

    @Mapping(source = "device.id", target = "deviceId")
    DeviceStatusResponse toResponseFromDeviceStatus(DeviceStatus deviceStatus);
}
