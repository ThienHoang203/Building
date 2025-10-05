package com.management.building.service.device;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.device.DeviceCreateRequest;
import com.management.building.dto.request.device.DeviceUpdateCategoryRequest;
import com.management.building.dto.request.device.DeviceUpdateRequest;
import com.management.building.dto.response.device.DeviceResponse;
import com.management.building.dto.response.device.DeviceResponseWithCategory;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.device.DeviceMapper;
import com.management.building.repository.device.CategoryRepository;
import com.management.building.repository.device.DeviceRepository;
import com.management.building.repository.device.SupplierRepository;
import com.management.building.repository.space.SpaceRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DeviceServiceImplement implements DeviceService {

    DeviceRepository deviceRepo;
    CategoryRepository categoryRepo;
    SupplierRepository supplierRepo;
    SpaceRepository spaceRepo;
    DeviceMapper deviceMapper;

    @Override
    public DeviceResponse create(DeviceCreateRequest requestBody) {
        String categoryCode = requestBody.getCategoryCode();
        if (!categoryRepo.existsById(categoryCode)) {
            throw new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND);
        }
        String supplierCode = requestBody.getSupplierCode();
        if (!supplierRepo.existsById(supplierCode)) {
            throw new AppException(ErrorCode.SUPPLIER_NOT_FOUND);
        }
        Long spaceId = requestBody.getSpaceId();
        if (spaceId != null && !spaceRepo.existsById(spaceId)) {
            throw new AppException(ErrorCode.SPACE_NOT_FOUND);
        }

        var entity = deviceMapper.toDeviceFromCreateRequest(requestBody);
        var result = deviceRepo.save(entity);
        return deviceMapper.toResponseFromDevice(result);
    }

    @Override
    public DeviceResponse update(String id, DeviceUpdateRequest requestBody) {
        var entity = deviceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND));
        deviceMapper.updateDeviceFromUpdateRequest(requestBody, entity);
        var result = deviceRepo.save(entity);
        return deviceMapper.toResponseFromDevice(result);
    }

    @Override
    public void deleteById(String id) {
        if (!deviceRepo.existsById(id)) {
            throw new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND);
        }
        try {
            deviceRepo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DeviceResponse getById(String id) {
        var result = deviceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND));
        return deviceMapper.toResponseFromDevice(result);
    }

    @Override
    public List<DeviceResponse> getAll() {
        var result = deviceRepo.findAll();
        return result.stream().map(deviceMapper::toResponseFromDevice).toList();
    }

    @Override
    public DeviceResponseWithCategory updateCategory(String id, DeviceUpdateCategoryRequest requestBody) {
        var device = deviceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND));
        var category = categoryRepo.findById(requestBody.getCategoryCode())
                .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND));
        device.setCategory(category);
        try {
            deviceRepo.save(device);
            return deviceMapper.toResponseWithCategoryFromDevice(device);
        } catch (Exception e) {
            throw new RuntimeException(String.format("update categoty code: %s into device id: %s failed",
                    requestBody.getCategoryCode(), id));
        }

    }

}
