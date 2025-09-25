package com.management.building.mapper.smartDevice;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.smartDevice.CategoryCreateRequest;
import com.management.building.dto.request.smartDevice.CategoryUpdateRequest;
import com.management.building.dto.response.smartDevice.CategoryResponse;
import com.management.building.entity.smartDevice.SmartDeviceCategory;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    SmartDeviceCategory toCategoryFromCreateRequest(CategoryCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SmartDeviceCategory toSupplierFromUpdateRequest(CategoryUpdateRequest updateRequest);

    CategoryResponse toResponseFromCategory(SmartDeviceCategory category);
}
