package com.management.building.service.smartDevice;

import java.util.List;

import com.management.building.dto.request.smartDevice.SupplierCreateRequest;
import com.management.building.dto.request.smartDevice.SupplierUpdateRequest;
import com.management.building.dto.response.smartDevice.SupplierResponse;

public interface SupplierService {

    SupplierResponse create(SupplierCreateRequest requestBody);

    List<SupplierResponse> getAll();

    SupplierResponse getByid(Long id);

    void deleteById(Long id);

    SupplierResponse updateById(Long id, SupplierUpdateRequest requestBody);

}
