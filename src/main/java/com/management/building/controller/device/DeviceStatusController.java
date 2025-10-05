package com.management.building.controller.device;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.device.DeviceStatusCreateRequest;
import com.management.building.dto.response.app.ApiResponse;
import com.management.building.dto.response.device.DeviceStatusResponse;
import com.management.building.service.device.DeviceStatusService;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@Validated
@RequestMapping("/device-status")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceStatusController {
    DeviceStatusService deviceStatusService;

    @PostMapping
    public ApiResponse<DeviceStatusResponse> create(@RequestParam("device_id") @NotBlank String deviceId,
            @RequestBody DeviceStatusCreateRequest requestBody) {
        var result = deviceStatusService.create(deviceId, requestBody);
        return ApiResponse.<DeviceStatusResponse>builder()
                .data(result)
                .code(200)
                .build();
    }

    @PostMapping("/category/{categoryCode}")
    public ApiResponse<List<DeviceStatusResponse>> createByCategoryCode(
            @PathVariable @NotBlank String categoryCode) {
        var result = deviceStatusService.createByCategoryCode(categoryCode);
        return ApiResponse.<List<DeviceStatusResponse>>builder()
                .data(result)
                .code(200)
                .build();
    }
}
