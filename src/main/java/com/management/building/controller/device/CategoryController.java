package com.management.building.controller.device;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.device.CategoryCreateRequest;
import com.management.building.dto.response.app.ApiResponse;
import com.management.building.dto.response.device.CategoryResponse;
import com.management.building.service.device.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Validated
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryResponse> create(@RequestBody @Valid CategoryCreateRequest requestBody) {
        var result = categoryService.create(requestBody);
        return ApiResponse.<CategoryResponse>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll() {
        var result = categoryService.getAll();
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @GetMapping("/{code}")
    public ApiResponse<CategoryResponse> getByCode(@PathVariable @NotBlank String code) {
        var result = categoryService.getByCode(code);
        return ApiResponse.<CategoryResponse>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @DeleteMapping("/{code}")
    public ApiResponse<Void> deleteByCode(String code) {
        categoryService.deleteByCode(code);
        return ApiResponse.<Void>builder()
                .message(String.format("Delete category code: %s successfully", code))
                .isSuccess(true)
                .build();
    }
}
