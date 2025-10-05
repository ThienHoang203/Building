package com.management.building.service.device;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.device.CategoryCreateRequest;
import com.management.building.dto.request.device.CategoryStatusCreateRequest;
import com.management.building.dto.response.device.CategoryResponse;
import com.management.building.dto.response.device.CategoryResponseWithStatus;
import com.management.building.entity.device.Category;
import com.management.building.entity.device.CategoryStatus;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.device.CategoryMapper;
import com.management.building.mapper.device.CategoryStatusMapper;
import com.management.building.repository.device.CategoryRepository;
import com.management.building.repository.device.CategoryStatusRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryServiceImplement implements CategoryService {
    CategoryRepository categoryRepo;
    CategoryStatusRepository categoryStatusRepo;
    CategoryMapper categoryMapper;
    CategoryStatusMapper categoryStatusMapper;

    @Override
    public CategoryResponse create(CategoryCreateRequest requestBody) {
        if (categoryRepo.existsByCodeAndName(requestBody.getCode(), requestBody.getName())) {
            throw new RuntimeException(String.format("There is the record for code: %s and name: %s",
                    requestBody.getCode(), requestBody.getName()));
        }
        var entity = categoryMapper.toCategoryFromCreateRequest(requestBody);
        var result = categoryRepo.save(entity);
        return categoryMapper.toReponseFromCategory(result);
    }

    @Override
    public void deleteByCode(String code) {
        if (!categoryRepo.existsById(code)) {
            throw new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND);
        }
        categoryRepo.deleteById(code);
    }

    @Override
    public List<CategoryResponse> getAll() {
        var result = categoryRepo.findAll();
        return result.stream().map(categoryMapper::toReponseFromCategory).toList();
    }

    @Override
    public CategoryResponse getByCode(String code) {
        var result = categoryRepo.findById(code)
                .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND));
        return categoryMapper.toReponseFromCategory(result);
    }

    @Override
    @Transactional
    public CategoryResponseWithStatus addStatusIntoCategory(String code,
            List<CategoryStatusCreateRequest> requestBody) {
        var category = categoryRepo.findById(code)
                .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND));
        if (category.getCategoryStatus() == null) {
            category.setCategoryStatus(new HashSet<>());
        }
        // select status's code which exists in the category
        Set<String> existCodes = new HashSet<>(category.getCategoryStatus().size());
        for (CategoryStatus categoryStatus : category.getCategoryStatus()) {
            existCodes.add(categoryStatus.getCode());
        }
        List<CategoryStatus> statusList = new ArrayList<>(requestBody.size());
        for (CategoryStatusCreateRequest createRequest : requestBody) {
            if (existCodes.contains(createRequest.getCode())) {
                continue;
            }
            var categoryStatus = categoryStatusMapper.toCategoryStatusFromCreateRequest(createRequest);
            categoryStatus.setCategory(category);
            statusList.add(categoryStatus);
        }
        try {
            var result = categoryStatusRepo.saveAll(statusList);
            var responseBody = CategoryResponseWithStatus.builder()
                    .categoryStatus(new LinkedHashSet<>(
                            result.stream().map(categoryStatusMapper::toResponseFromCategoryStatus).toList()))
                    .code(code)
                    .build();
            return responseBody;
        } catch (Exception e) {
            log.warn(String.format("Error occurred when add status into category code: %s, cause: %s", code,
                    e.getMessage()));
            throw new RuntimeException(String.format("Error occurred when add status into category code: %s", code));
        }
    }

    @Override
    public List<CategoryResponse> createBulkCategory(List<CategoryCreateRequest> requestBody) {
        List<Category> categories = requestBody.stream().map(categoryMapper::toCategoryFromCreateRequest).toList();
        try {
            var result = categoryRepo.saveAll(categories);
            return result.stream().map(categoryMapper::toReponseFromCategory).toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bulk of categories");
        }
    }

    @Override
    public CategoryResponseWithStatus getStatusOfCategory(String code) {
        var result = categoryRepo.findById(code)
                .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND));
        return categoryMapper.toResponseWithStatusFromCategory(result);
    }

}
