package com.management.building.controller.tuya.device;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.response.app.ApiResponse;
import com.management.building.dto.response.device.DeviceResponse;
import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaDeviceDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaLogResult;
import com.management.building.dto.response.tuyaCloud.device.TuyaPropertyResult;
import com.management.building.dto.response.tuyaCloud.device.TuyaStatusOfMulDevicesResponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaStatusResponse;
import com.management.building.service.tuya.device.TuyaDeviceService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@Validated
@RequestMapping("/tuya-devices")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TuyaDeviceController {
    TuyaDeviceService deviceService;

    @GetMapping
    public TuyaReponse<List<TuyaDeviceDetail>> getDeviceInProject(
            @RequestParam(name = "product_ids", required = false) String productId,
            @RequestParam(required = false) String categories,
            @RequestParam(name = "last_id", required = false) String lastId,
            @RequestParam(defaultValue = "5", name = "page_size") @Min(value = 1) @Max(value = 20) Integer pageSize) {

        var result = deviceService.getDivicesInProject(
                productId,
                categories,
                lastId,
                pageSize);
        return result;
    }

    @GetMapping("/{id}/log")
    public TuyaReponse<TuyaLogResult> getDeviceStatus(
            @PathVariable String id,
            @RequestParam(defaultValue = "", required = false) String codes,
            @RequestParam(name = "start_time", required = false) Long startTime,
            @RequestParam(name = "end_time", required = false) Long endTime,
            @RequestParam(name = "last_row_key", required = false, defaultValue = "") String lastRowKey,
            @RequestParam(defaultValue = "20", required = false) Integer size) {
        TuyaReponse<TuyaLogResult> result = deviceService.getLogByDeviceId(
                id,
                codes,
                startTime,
                endTime,
                lastRowKey,
                size);
        return result;
    }

    @GetMapping("/{id}/status")
    public TuyaReponse<List<TuyaStatusResponse>> getStatusOfADevice(@PathVariable @NotBlank String id) {
        var result = deviceService.getStatusOfASingleDevice(id);
        return result;
    }

    @GetMapping("/{id}/properties")
    public TuyaReponse<TuyaPropertyResult> getMethodName(@PathVariable String id) {
        return deviceService.getPropertiesOfDevice(id);
    }

    @GetMapping("/{id}")
    public TuyaReponse<TuyaDeviceDetail> getADevice(@PathVariable @NotBlank String id) {
        var result = deviceService.getADevice(id);
        return result;
    }

    @GetMapping("status")
    public TuyaReponse<List<TuyaStatusOfMulDevicesResponse>> getStatusOfMulDevices(
            @RequestParam(name = "device_ids", required = true) @NotBlank String deviceIds) {
        var result = deviceService.getStatusOfMulDevices(deviceIds);
        return result;
    }

    @PostMapping("/project/save")
    public ApiResponse<List<DeviceResponse>> saveDevicesInAProject() {
        var result = deviceService.saveDevicesInAProject();
        return ApiResponse.<List<DeviceResponse>>builder()
                .data(result)
                .code(200)
                .build();
    }

}