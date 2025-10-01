package com.management.building.service.device;

import java.util.List;

import com.management.building.dto.request.device.DeviceCreateRequest;
import com.management.building.dto.request.device.DeviceUpdateRequest;
import com.management.building.dto.response.device.DeviceResponse;

public interface DeviceService {
    DeviceResponse create(DeviceCreateRequest requestBody);

    DeviceResponse update(String id, DeviceUpdateRequest requestBody);

    void deleteById(String id);

    DeviceResponse getById(String id);

    List<DeviceResponse> getAll();
}
