package com.management.building.service.device;

import java.util.List;

import com.management.building.dto.request.device.DeviceStatusCreateRequest;
import com.management.building.dto.response.device.DeviceStatusResponse;

public interface DeviceStatusService {
    DeviceStatusResponse create(String deviceId, DeviceStatusCreateRequest request);

    List<DeviceStatusResponse> bulkSave(String deviceId, List<DeviceStatusCreateRequest> request);

    List<DeviceStatusResponse> createByCategoryCode(String categoryCode);
}
