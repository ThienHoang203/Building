package com.management.building.mapper.device;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.device.DeviceCreateRequest;
import com.management.building.dto.request.device.DeviceUpdateRequest;
import com.management.building.dto.response.device.DeviceResponse;
import com.management.building.dto.response.device.DeviceResponseWithCategory;
import com.management.building.entity.device.Device;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    @Mapping(source = "category.code", target = "category")
    DeviceResponse toResponseFromDevice(Device device);

    @Mapping(source = "id", target = "deviceId")
    DeviceResponseWithCategory toResponseWithCategoryFromDevice(Device device);

    Device toDeviceFromCreateRequest(DeviceCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeviceFromUpdateRequest(DeviceUpdateRequest createRequest, @MappingTarget Device device);
}
