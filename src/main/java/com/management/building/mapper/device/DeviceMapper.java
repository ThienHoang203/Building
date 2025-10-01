package com.management.building.mapper.device;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.device.DeviceCreateRequest;
import com.management.building.dto.request.device.DeviceUpdateRequest;
import com.management.building.dto.response.device.DeviceResponse;
import com.management.building.entity.device.Device;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceResponse toResponseFromDevice(Device device);

    Device toDeviceFromCreateRequest(DeviceCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeviceFromUpdateRequest(DeviceUpdateRequest createRequest, @MappingTarget Device device);
}
