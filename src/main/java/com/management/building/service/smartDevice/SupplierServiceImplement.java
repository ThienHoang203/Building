package com.management.building.service.smartDevice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.smartDevice.SupplierCreateRequest;
import com.management.building.dto.request.smartDevice.SupplierUpdateRequest;
import com.management.building.dto.response.smartDevice.SupplierResponse;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.smartDevice.SupplierMapper;
import com.management.building.repository.smartDevice.SupplierRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SupplierServiceImplement implements SupplierService {
    SupplierRepository supplierRepo;
    SupplierMapper supplierMapper;

    @Override
    public SupplierResponse create(SupplierCreateRequest requestBody) {
        if (supplierRepo.existsByCode(requestBody.getCode())) {
            throw new AppException(ErrorCode.SUPPLER_CODE_EXISTS);
        }
        requestBody.setCode(requestBody.getCode().trim().toUpperCase());
        requestBody.setName(requestBody.getName().trim());
        var entity = supplierMapper.toSupplierFromCreateRequest(requestBody);
        var result = supplierRepo.save(entity);
        return supplierMapper.toResponseFromSupplier(result);
    }

    @Override
    public List<SupplierResponse> getAll() {
        var result = supplierRepo.findAll();
        return result.stream().map(supplierMapper::toResponseFromSupplier).toList();
    }

    @Override
    public SupplierResponse getByid(Long id) {
        var result = supplierRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        return supplierMapper.toResponseFromSupplier(result);
    }

    @Override
    public void deleteById(Long id) {
        if (!supplierRepo.existsById(id)) {
            throw new AppException(ErrorCode.SUPPLIER_NOT_FOUND);
        }
        try {
            supplierRepo.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete supplier with ID: " + id + ", message: " + e.getMessage() + "\n", e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    public SupplierResponse updateById(Long id, SupplierUpdateRequest requestBody) {
        var entity = supplierRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        //validate name
        String name = requestBody.getName();
        if (name != null) {
            name = name.trim();
            int length = name.length();
            if (length > 100 || length < 2) {
                throw new AppException(ErrorCode.SUPPLIER_NAME_LENGTH_INVALID);
            }
            requestBody.setName(name);
        }
        // validate code
        String code = requestBody.getCode();
        if (code != null) {
            code = code.trim().toUpperCase();
            if (supplierRepo.existsByCode(requestBody.getCode())) {
                throw new AppException(ErrorCode.SUPPLER_CODE_EXISTS);
            }
            int length = code.length();
            if (length > 50 || length < 3) {
                throw new AppException(ErrorCode.SUPPLIER_CODE_LENGTH_INVALID);
            }
            requestBody.setCode(code);
        }
        entity.setCode(requestBody.getCode());
        entity.setName(requestBody.getName());
        var result = supplierRepo.save(entity);
        return supplierMapper.toResponseFromSupplier(result);
    }
}
