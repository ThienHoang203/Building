package com.management.building.mapper.device;

import org.mapstruct.Mapper;

import com.management.building.dto.request.device.CategoryStatusCreateRequest;
import com.management.building.dto.response.device.CategoryStatusResponse;
import com.management.building.entity.device.CategoryStatus;

@Mapper(componentModel = "spring")
public interface CategoryStatusMapper {
    CategoryStatus toCategoryStatusFromCreateRequest(CategoryStatusCreateRequest createRequest);

    CategoryStatusResponse toResponseFromCategoryStatus(CategoryStatus categoryStatus);
}
