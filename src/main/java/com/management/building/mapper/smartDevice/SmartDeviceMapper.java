package com.management.building.mapper.smartDevice;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.smartDevice.SmartDeviceCreateRequest;
import com.management.building.dto.request.smartDevice.SmartDeviceUpdateRequest;
import com.management.building.dto.response.smartDevice.SmartDeviceResponse;
import com.management.building.entity.smartDevice.SmartDevice;

@Mapper(componentModel = "spring")
public interface SmartDeviceMapper {
    SmartDevice toSmartDeviceFromCreateRequest(SmartDeviceCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SmartDevice toSmartDeviceFromUpdateRequest(SmartDeviceUpdateRequest updateRequest);

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "spaceId", source = "space.id")
    SmartDeviceResponse toResponseFromSmartDevice(SmartDevice category);
}
