package com.management.building.controller.device;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.device.SupplierCreateRequest;
import com.management.building.dto.response.app.ApiResponse;
import com.management.building.dto.response.device.SupplierResponse;
import com.management.building.service.device.SupplierService;

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
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierController {

    SupplierService supplierService;

    @PostMapping
    public ApiResponse<SupplierResponse> create(@RequestBody @Valid SupplierCreateRequest requestBody) {
        var result = supplierService.create(requestBody);
        return ApiResponse.<SupplierResponse>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @GetMapping
    public ApiResponse<List<SupplierResponse>> getAll() {
        var result = supplierService.getAll();
        return ApiResponse.<List<SupplierResponse>>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @GetMapping("/{code}")
    public ApiResponse<SupplierResponse> getByCode(@PathVariable @NotBlank String code) {
        var result = supplierService.getByCode(code);
        return ApiResponse.<SupplierResponse>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @DeleteMapping("/{code}")
    public ApiResponse<Void> deleteByCode(String code) {
        supplierService.deleteByCode(code);
        return ApiResponse.<Void>builder()
                .message(String.format("Delete category code: %s successfully", code))
                .isSuccess(true)
                .build();
    }

}
