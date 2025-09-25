package com.management.building.service.smartDevice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.smartDevice.CategoryCreateRequest;
import com.management.building.dto.response.smartDevice.CategoryResponse;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.smartDevice.CategoryMapper;
import com.management.building.repository.smartDevice.SmartDeviceCategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SmartDeviceCategoryServiceImplement implements SmartDeviceCategoryService {
    SmartDeviceCategoryRepository categoryRepo;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryCreateRequest requestBody) {
        requestBody.setName(requestBody.getName().trim().toUpperCase());
        if (categoryRepo.existsById(requestBody.getName())) {
            throw new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NAME_EXISTS);
        }
        var entity = categoryMapper.toCategoryFromCreateRequest(requestBody);
        var result = categoryRepo.save(entity);
        return categoryMapper.toResponseFromCategory(result);
    }

    @Override
    public List<CategoryResponse> getAll() {
        var result = categoryRepo.findAll();
        return result.stream().map(categoryMapper::toResponseFromCategory).toList();
    }

    @Override
    public CategoryResponse getByName(String name) {
        var result = categoryRepo.findById(name.trim().toUpperCase())
                .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND));
        return categoryMapper.toResponseFromCategory(result);
    }

    @Override
    public void deleteByName(String name) {
        if (!categoryRepo.existsById(name)) {
            throw new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND);
        }
        try {
            categoryRepo.deleteById(name);
        } catch (Exception e) {
            log.error(
                    "Failed to delete smart device category with Name: " + name + ", message: " + e.getMessage() + "\n",
                    e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

}
