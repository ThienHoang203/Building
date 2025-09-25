package com.management.building.service.smartDevice;

import java.util.List;

import com.management.building.dto.request.smartDevice.CategoryCreateRequest;
import com.management.building.dto.response.smartDevice.CategoryResponse;

public interface SmartDeviceCategoryService {

    CategoryResponse create(CategoryCreateRequest requestBody);

    List<CategoryResponse> getAll();

    CategoryResponse getByName(String name);

    void deleteByName(String name);

}
