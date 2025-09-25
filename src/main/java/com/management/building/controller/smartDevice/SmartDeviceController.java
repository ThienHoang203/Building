package com.management.building.controller.smartDevice;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.smartDevice.SmartDeviceCreateRequest;
import com.management.building.dto.request.smartDevice.SmartDeviceUpdateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.smartDevice.SmartDeviceResponse;
import com.management.building.service.smartDevice.SmartDeviceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Validated
@RequestMapping("/smart-devices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SmartDeviceController {
    SmartDeviceService deviceService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ApiResponse<SmartDeviceResponse> postMethodName(@RequestBody @Valid SmartDeviceCreateRequest requestBody) {
        SmartDeviceResponse result = deviceService.create(requestBody);
        return ApiResponse.<SmartDeviceResponse>builder()
                .code(201)
                .data(result)
                .build();
    }

    @GetMapping
    public ApiResponse<List<SmartDeviceResponse>> getAll() {
        List<SmartDeviceResponse> result = deviceService.getAll();
        return ApiResponse.<List<SmartDeviceResponse>>builder()
                .code(200)
                .data(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SmartDeviceResponse> getById(@PathVariable @Min(value = 1) Long id) {
        SmartDeviceResponse result = deviceService.getById(id);
        return ApiResponse.<SmartDeviceResponse>builder()
                .code(200)
                .data(result)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteById(@PathVariable @Min(value = 1) Long id) {
        deviceService.deleteById(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message(String.format("Delete supplier ID = %d successfully", id))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SmartDeviceResponse> updateById(@PathVariable @Min(value = 1) Long id,
            @RequestBody @Valid SmartDeviceUpdateRequest requestBody) {
        SmartDeviceResponse result = deviceService.updateById(id, requestBody);
        return ApiResponse.<SmartDeviceResponse>builder()
                .code(200)
                .data(result)
                .build();
    }
}
