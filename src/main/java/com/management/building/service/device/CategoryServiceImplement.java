package com.management.building.service.device;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.device.CategoryCreateRequest;
import com.management.building.dto.response.device.CategoryResponse;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.device.CategoryMapper;
import com.management.building.repository.device.CategoryRepository;

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
    CategoryMapper categoryMapper;

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

}
