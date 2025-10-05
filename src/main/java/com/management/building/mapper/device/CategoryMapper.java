package com.management.building.mapper.device;

import org.mapstruct.Mapper;

import com.management.building.dto.request.device.CategoryCreateRequest;
import com.management.building.dto.response.device.CategoryResponse;
import com.management.building.dto.response.device.CategoryResponseWithStatus;
import com.management.building.entity.device.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategoryFromCreateRequest(CategoryCreateRequest createRequest);

    CategoryResponse toReponseFromCategory(Category category);

    CategoryResponseWithStatus toResponseWithStatusFromCategory(Category category);
}
