package com.management.building.controller.smartDevice;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.smartDevice.SupplierCreateRequest;
import com.management.building.dto.request.smartDevice.SupplierUpdateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.smartDevice.SupplierResponse;
import com.management.building.service.smartDevice.SupplierService;

import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@Validated
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SupplierController {
    SupplierService supplierService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SupplierResponse> create(@RequestBody @Valid SupplierCreateRequest requestBody) {

        SupplierResponse result = supplierService.create(requestBody);

        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .data(result)
                .build();
    }

    @GetMapping
    public ApiResponse<List<SupplierResponse>> getAll() {
        List<SupplierResponse> result = supplierService.getAll();
        return ApiResponse.<List<SupplierResponse>>builder()
                .code(200)
                .data(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SupplierResponse> getById(@PathVariable @Nonnull @Min(value = 1) Long id) {
        SupplierResponse result = supplierService.getByid(id);
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .data(result)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable @Nonnull @Min(value = 1) Long id) {
        supplierService.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message(String.format("Delete supplier ID = %d successfully", id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SupplierResponse> updateById(@PathVariable @Min(value = 1) Long id,
            @RequestBody @Valid SupplierUpdateRequest requestBody) {
        SupplierResponse result = supplierService.updateById(id, requestBody);
        return ApiResponse.<SupplierResponse>builder()
                .code(200)
                .data(result)
                .build();
    }
}
