package com.management.building.service.device;

import java.util.List;

import com.management.building.dto.request.device.CategoryCreateRequest;
import com.management.building.dto.request.device.CategoryStatusCreateRequest;
import com.management.building.dto.response.device.CategoryResponse;
import com.management.building.dto.response.device.CategoryResponseWithStatus;

public interface CategoryService {
    CategoryResponse create(CategoryCreateRequest requestBody);

    void deleteByCode(String code);

    List<CategoryResponse> getAll();

    CategoryResponse getByCode(String code);

    CategoryResponseWithStatus addStatusIntoCategory(String code, List<CategoryStatusCreateRequest> requestBody);

    CategoryResponseWithStatus getStatusOfCategory(String code);

    List<CategoryResponse> createBulkCategory(List<CategoryCreateRequest> requestBody);
}
