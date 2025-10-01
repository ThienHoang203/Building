package com.management.building.service.device;

import java.util.List;

import org.springframework.stereotype.Service;

import com.management.building.dto.request.device.SupplierCreateRequest;
import com.management.building.dto.response.device.SupplierResponse;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.device.SupplierMapper;
import com.management.building.repository.device.SupplierRepository;

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
        if (supplierRepo.existsByCodeAndName(requestBody.getCode(), requestBody.getName())) {
            throw new RuntimeException(String.format("There is the record for code: %s and name: %s",
                    requestBody.getCode(), requestBody.getName()));
        }
        var entity = supplierMapper.toSupplierFromCreateRequest(requestBody);
        var result = supplierRepo.save(entity);
        return supplierMapper.toReponseFromCSupplier(result);
    }

    @Override
    public void deleteByCode(String code) {
        if (!supplierRepo.existsById(code)) {
            throw new AppException(ErrorCode.SUPPLIER_NOT_FOUND);
        }
        supplierRepo.deleteById(code);
    }

    @Override
    public List<SupplierResponse> getAll() {
        var result = supplierRepo.findAll();
        return result.stream().map(supplierMapper::toReponseFromCSupplier).toList();
    }

    @Override
    public SupplierResponse getByCode(String code) {
        var result = supplierRepo.findById(code)
                .orElseThrow(() -> new AppException(ErrorCode.SUPPLIER_NOT_FOUND));
        return supplierMapper.toReponseFromCSupplier(result);
    }

}
