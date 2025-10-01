package com.management.building.controller.device;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.device.DeviceCreateRequest;
import com.management.building.dto.request.device.DeviceUpdateRequest;
import com.management.building.dto.response.app.ApiResponse;
import com.management.building.dto.response.device.DeviceResponse;
import com.management.building.service.device.DeviceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Validated
@RequestMapping("/devices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class DeviceController {

    DeviceService deviceService;

    @PostMapping
    public ApiResponse<DeviceResponse> create(@RequestBody @Valid DeviceCreateRequest requestBody) {

        var result = deviceService.create(requestBody);

        return ApiResponse.<DeviceResponse>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DeviceResponse> update(@PathVariable @NotBlank String id,
            @RequestBody DeviceUpdateRequest requestBody) {
        var result = deviceService.update(id, requestBody);
        return ApiResponse.<DeviceResponse>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @NotBlank String id) {
        return ApiResponse.<Void>builder()
                .message(String.format("Delete the device with ID: %s successfully", id))
                .isSuccess(true)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DeviceResponse> getById(@PathVariable @NotBlank String id) {
        var result = deviceService.getById(id);
        return ApiResponse.<DeviceResponse>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

    @GetMapping
    public ApiResponse<List<DeviceResponse>> getAll() {
        var result = deviceService.getAll();
        return ApiResponse.<List<DeviceResponse>>builder()
                .data(result)
                .isSuccess(true)
                .build();
    }

}
