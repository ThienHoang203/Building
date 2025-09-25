package com.management.building.controller.smartDevice;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.smartDevice.CategoryCreateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.smartDevice.CategoryResponse;
import com.management.building.service.smartDevice.SmartDeviceCategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@Validated
@RequestMapping("/smart-device-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SmartDeviceCategoryController {

    SmartDeviceCategoryService categoryService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ApiResponse<CategoryResponse> postMethodName(@RequestBody @Valid CategoryCreateRequest requestBody) {

        CategoryResponse result = categoryService.create(requestBody);

        return ApiResponse.<CategoryResponse>builder()
                .code(201)
                .data(result)
                .build();
    }

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAll() {
        List<CategoryResponse> result = categoryService.getAll();
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .data(result)
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<CategoryResponse> getByName(@PathVariable @Size(min = 2, max = 100) String name) {
        CategoryResponse result = categoryService.getByName(name);
        return ApiResponse.<CategoryResponse>builder()
                .code(200)
                .data(result)
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<Void> deleteByName(@PathVariable @Size(min = 2, max = 100) String name) {
        categoryService.deleteByName(name);
        return ApiResponse.<Void>builder()
                .code(200)
                .message(String.format("Delete category with Name = %s successfully", name))
                .build();
    }
}
