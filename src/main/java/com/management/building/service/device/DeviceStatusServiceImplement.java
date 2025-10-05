package com.management.building.service.device;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.device.DeviceStatusCreateRequest;
import com.management.building.dto.response.device.DeviceStatusResponse;
import com.management.building.entity.device.CategoryStatus;
import com.management.building.entity.device.Device;
import com.management.building.entity.device.DeviceStatus;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.device.DeviceStatusMapper;
import com.management.building.repository.device.CategoryRepository;
import com.management.building.repository.device.CategoryStatusRepository;
import com.management.building.repository.device.DeviceRepository;
import com.management.building.repository.device.DeviceStatusRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DeviceStatusServiceImplement implements DeviceStatusService {
    DeviceStatusRepository deviceStatusRepository;
    DeviceRepository deviceRepository;
    CategoryStatusRepository categoryStatusRepository;
    CategoryRepository categoryRepository;
    DeviceStatusMapper deviceStatusMapper;

    @Override
    public DeviceStatusResponse create(String deviceId, DeviceStatusCreateRequest request) {
        try {
            var device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND));
            if (device.getCategory() == null) {
                throw new RuntimeException(String.format(
                        "failed to create new device status, cause: device with ID: %s is not belong to any category",
                        deviceId));
            }
            if (!categoryStatusRepository.existsById(request.getStatusCode())) {
                throw new RuntimeException(String.format("the status code: %s in category: %s does not exist",
                        request.getStatusCode(), device.getCategory().getCode()));
            }
            var entity = deviceStatusMapper.toStatusFromCreateRequest(request);
            entity.setDevice(device);
            var result = deviceStatusRepository.save(entity);
            return deviceStatusMapper.toResponseFromDeviceStatus(result);
        } catch (RuntimeException e) {
            throw new RuntimeException(String.format("failed to create new device status, cause: %s", e.getMessage()));
        }
    }

    @Override
    public List<DeviceStatusResponse> bulkSave(String deviceId, List<DeviceStatusCreateRequest> request) {
        try {
            var device = deviceRepository.findById(deviceId)
                    .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND));
            if (device.getCategory() == null) {
                throw new RuntimeException(String.format(
                        "failed to create new device status, cause: device with ID: %s is not belong to any category",
                        deviceId));
            }

            // get all statusCode meet the request
            Set<String> statusCodes = new HashSet<>(request.size());
            request.forEach(r -> statusCodes.add(r.getStatusCode()));
            List<CategoryStatus> status = categoryStatusRepository.findAllStatusCodeInRangeByCategoryCode(
                    device.getCategory().getCode(),
                    statusCodes);
            Set<String> statusSet = new HashSet<>(status.size());
            status.forEach(s -> statusSet.add(s.getCode()));

            List<DeviceStatus> entities = new ArrayList<>(request.size());
            request.forEach(r -> {
                var entity = deviceStatusMapper.toStatusFromCreateRequest(r);
                entity.setDevice(device);
                entity.setStatusCode(r.getStatusCode());
                entities.add(entity);
            });
            var result = deviceStatusRepository.saveAll(entities);
            return result.stream().map(deviceStatusMapper::toResponseFromDeviceStatus).toList();
        } catch (RuntimeException e) {
            throw new RuntimeException(
                    String.format("failed to create new bulk of device status, cause: %s", e.getMessage()));
        }
    }

    @Override
    public List<DeviceStatusResponse> createByCategoryCode(String categoryCode) {
        var category = categoryRepository.findById(categoryCode)
                .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND));
        var devices = category.getDevices();
        var statuses = category.getCategoryStatus();
        if (statuses.size() == 0) {
            return null;
        }
        List<DeviceStatus> deviceStatuses = new ArrayList<>(devices.size() * statuses.size());
        for (Device device : devices) {
            for (DeviceStatus deviceStatus : deviceStatuses) {
                var newDeviceStatus = DeviceStatus.builder()
                        .device(device)
                        .statusCode(deviceStatus.getStatusCode())
                        .value("")
                        .build();
                deviceStatuses.add(newDeviceStatus);
            }
        }
        try {
            var result = deviceStatusRepository.saveAll(deviceStatuses);
            return result.stream().map(deviceStatusMapper::toResponseFromDeviceStatus).toList();
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Failed to create device status set by category code, cause %s", e.getMessage()));
        }
    }

}
