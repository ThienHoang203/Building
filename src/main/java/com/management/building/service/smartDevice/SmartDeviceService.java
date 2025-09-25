package com.management.building.service.smartDevice;

import java.util.List;

import com.management.building.dto.request.smartDevice.SmartDeviceCreateRequest;
import com.management.building.dto.request.smartDevice.SmartDeviceUpdateRequest;
import com.management.building.dto.response.smartDevice.SmartDeviceResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface SmartDeviceService {
    SmartDeviceResponse create(SmartDeviceCreateRequest requestBody);

    List<SmartDeviceResponse> getAll();

    SmartDeviceResponse getById(Long id);

    void deleteById(Long id);

    SmartDeviceResponse updateById(Long id, SmartDeviceUpdateRequest requestBody);

}
