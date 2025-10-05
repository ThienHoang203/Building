package com.management.building.service.tuya.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.management.building.dto.response.device.DeviceResponse;
import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaDeviceDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaLogResult;
import com.management.building.dto.response.tuyaCloud.device.TuyaPropertyResult;
import com.management.building.dto.response.tuyaCloud.device.TuyaStatusOfMulDevicesResponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaStatusResponse;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.device.DeviceMapper;
import com.management.building.mapper.tuya.TuyaMapper;
import com.management.building.repository.device.DeviceRepository;
import com.management.building.repository.device.SupplierRepository;
import com.management.building.service.tuya.TuyaApiClient;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TuyaDeviceServiceImplement implements TuyaDeviceService {
    TuyaApiClient apiClient;
    DeviceRepository deviceRepository;
    SupplierRepository supplierRepository;
    TuyaMapper tuyaMapper;
    DeviceMapper deviceMapper;

    @Override
    public TuyaReponse<List<TuyaDeviceDetail>> getDivicesInProject(
            String productId,
            String categories,
            String lastId,
            Integer pageSize) {
        try {
            // Build query parameters
            Map<String, String> queryParams = new HashMap<>();

            if (StringUtils.hasText(productId)) {
                queryParams.put("product_ids", productId);
            }

            if (StringUtils.hasText(categories)) {
                queryParams.put("categories", categories);
            }

            if (StringUtils.hasText(lastId)) {
                queryParams.put("last_id", lastId);
            }

            queryParams.put("page_size", pageSize.toString());

            String url = "/v2.0/cloud/thing/device";

            TuyaReponse<List<TuyaDeviceDetail>> response = apiClient.get(
                    url,
                    new ParameterizedTypeReference<TuyaReponse<List<TuyaDeviceDetail>>>() {
                    },
                    queryParams);

            if (response != null && response.getSuccess()) {
                log.info("Successfully retrieved {} devices",
                        response.getResult() != null ? response.getResult().size() : 0);
            } else {
                log.warn("Failed to get devices: {}",
                        response != null ? response.getMsg() : "Unknown error");
            }

            return response;

        } catch (Exception e) {
            log.error("Error getting devices in project", e);
            throw new RuntimeException("Error getting devices in project", e);
        }
    }

    @Override
    public TuyaReponse<List<TuyaStatusResponse>> getStatusOfASingleDevice(String id) {
        String url = String.format("/v1.0/iot-03/devices/%s/status", id);
        return apiClient.get(
                url,
                new ParameterizedTypeReference<TuyaReponse<List<TuyaStatusResponse>>>() {
                });
    }

    @Override
    public TuyaReponse<TuyaLogResult> getLogByDeviceId(String id, String codes, Long startTime, Long endTime,
            String lastRowKey,
            Integer size) {
        try {
            // Build query parameters
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("codes", !StringUtils.hasText(codes) ? "" : codes);
            queryParams.put("start_time", startTime.toString());
            queryParams.put("last_row_key", !StringUtils.hasText(lastRowKey) ? "" : lastRowKey);
            queryParams.put("end_time", endTime.toString());
            queryParams.put("size", size.toString());

            String url = String.format("/v2.1/cloud/thing/%s/report-logs", id);

            TuyaReponse<TuyaLogResult> response = apiClient.get(
                    url,
                    new ParameterizedTypeReference<TuyaReponse<TuyaLogResult>>() {
                    }, queryParams);

            if (response != null && response.getSuccess()) {
                log.info("Successfully retrieved {} devices",
                        response.getResult() != null ? response.getResult().getDevice_id() : 0);
            } else {
                log.warn("Failed to get devices: {}",
                        response != null ? response.getMsg() : "Unknown error");
            }

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error getting devices in project, cause: " + e.getMessage(), e);
        }
    }

    @Override
    public TuyaReponse<TuyaPropertyResult> getPropertiesOfDevice(String deviceId) {
        String url = String.format("/v2.0/cloud/thing/%s/shadow/properties", deviceId);
        return apiClient.get(
                url,
                new ParameterizedTypeReference<TuyaReponse<TuyaPropertyResult>>() {
                });
    }

    @Override
    public TuyaReponse<TuyaDeviceDetail> getADevice(String id) {
        String url = String.format("/v2.0/cloud/thing/%s", id);
        return apiClient.get(
                url,
                new ParameterizedTypeReference<TuyaReponse<TuyaDeviceDetail>>() {
                });
    }

    @Override
    @Transactional
    public List<DeviceResponse> saveDevicesInAProject() {
        var supplier = supplierRepository.findById("tyc")
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        var devices = getDivicesInProject(null, null, null, 20);
        var mappedDevices = devices.getResult().stream().map(e -> {
            var entity = tuyaMapper.toDeviceFromTuyaDeviceDetail(e);
            entity.setSupplier(supplier);
            return entity;
        }).toList();
        try {
            var result = deviceRepository.saveAll(mappedDevices);
            return result.stream().map(deviceMapper::toResponseFromDevice).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred when save all devices in a project from tuya cloud");
        }
    }

    @Override
    public TuyaReponse<List<TuyaStatusOfMulDevicesResponse>> getStatusOfMulDevices(String deviceIds) {
        var url = String.format("/v1.0/iot-03/devices/status?device_ids=%s", deviceIds);
        try {
            return apiClient.get(
                    url,
                    new ParameterizedTypeReference<TuyaReponse<List<TuyaStatusOfMulDevicesResponse>>>() {
                    });
        } catch (Exception e) {
            log.error(String.format("Error occurred when status of multiple devices from tuya cloud, cause %s",
                    e.getMessage()));
            throw new RuntimeException("Error occurred when status of multiple devices from tuya cloud");
        }
    }

}
