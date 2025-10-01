package com.management.building.service.device;

import java.util.List;

import com.management.building.dto.request.device.CategoryCreateRequest;
import com.management.building.dto.response.device.CategoryResponse;

public interface CategoryService {
    CategoryResponse create(CategoryCreateRequest requestBody);

    void deleteByCode(String code);

    List<CategoryResponse> getAll();

    CategoryResponse getByCode(String code);
}
