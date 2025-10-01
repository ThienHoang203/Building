package com.management.building.service.device;

import java.util.List;

import com.management.building.dto.request.device.SupplierCreateRequest;
import com.management.building.dto.response.device.SupplierResponse;

public interface SupplierService {

    SupplierResponse create(SupplierCreateRequest requestBody);

    void deleteByCode(String code);

    List<SupplierResponse> getAll();

    SupplierResponse getByCode(String code);

}
