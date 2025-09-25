package com.management.building.service.smartDevice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.smartDevice.SmartDeviceCreateRequest;
import com.management.building.dto.request.smartDevice.SmartDeviceUpdateRequest;
import com.management.building.dto.response.smartDevice.SmartDeviceResponse;
import com.management.building.entity.smartDevice.SmartDeviceCategory;
import com.management.building.entity.smartDevice.Supplier;
import com.management.building.entity.space.Space;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.smartDevice.SmartDeviceMapper;
import com.management.building.repository.smartDevice.SmartDeviceCategoryRepository;
import com.management.building.repository.smartDevice.SmartDeviceRepository;
import com.management.building.repository.smartDevice.SupplierRepository;
import com.management.building.repository.space.SpaceRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SmartDeviceServiceImplement implements SmartDeviceService {
    SmartDeviceRepository deviceRepo;
    SpaceRepository spaceRepo;
    SmartDeviceCategoryRepository categoryRepo;
    SupplierRepository supplierRepo;
    SmartDeviceMapper deviceMapper;

    @Override
    public SmartDeviceResponse create(SmartDeviceCreateRequest requestBody) {
        requestBody.setCode(requestBody.getCode().trim().toUpperCase());
        requestBody.setName(requestBody.getName().trim());
        requestBody.setCategoryName(requestBody.getCategoryName().trim().toUpperCase());

        // validate request
        if (deviceRepo.existsByName(requestBody.getName())) {
            throw new AppException(ErrorCode.SMART_DEVICE_NAME_EXISTS);
        }
        if (deviceRepo.existsByCode(requestBody.getCode())) {
            throw new AppException(ErrorCode.SMART_DEVICE_CODE_EXISTS);
        }
        SmartDeviceCategory category = categoryRepo.findById(requestBody.getCategoryName())
                .orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_CATEGORY_NOT_FOUND));
        Supplier supplier = supplierRepo.findById(requestBody.getSupplierId())
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        Space space = null;
        if (requestBody.getSpaceId() != null && requestBody.getSpaceId() > 0) {
            space = spaceRepo.findById(requestBody.getSpaceId())
                    .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));
        }

        var entity = deviceMapper.toSmartDeviceFromCreateRequest(requestBody);
        entity.setCategory(category);
        entity.setSupplier(supplier);
        entity.setSpace(space);
        var result = deviceRepo.save(entity);
        return deviceMapper.toResponseFromSmartDevice(result);
    }

    @Override
    public List<SmartDeviceResponse> getAll() {
        var result = deviceRepo.findAll();
        return result.stream().map(deviceMapper::toResponseFromSmartDevice).toList();
    }

    @Override
    public SmartDeviceResponse getById(Long id) {
        var result = deviceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND));
        return deviceMapper.toResponseFromSmartDevice(result);
    }

    @Override
    public void deleteById(Long id) {
        if (!deviceRepo.existsById(id)) {
            throw new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND);
        }
        try {
            deviceRepo.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete smart device with ID: " + id + ", message: " + e.getMessage() + "\n", e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    public SmartDeviceResponse updateById(Long id, SmartDeviceUpdateRequest requestBody) {
        var entity = deviceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SMART_DEVICE_NOT_FOUND));
        Space space = null;
        if (requestBody.getSpaceId() != null) {
            if (entity.getSpace() == null || requestBody.getSpaceId() != entity.getSpace().getId()) {
                space = spaceRepo.findById(requestBody.getSpaceId())
                        .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));
            }
        }
        if (space != null) {
            entity.setSpace(space);
            var result = deviceRepo.save(entity);
            return deviceMapper.toResponseFromSmartDevice(result);
        }
        return deviceMapper.toResponseFromSmartDevice(entity);
    }

}
