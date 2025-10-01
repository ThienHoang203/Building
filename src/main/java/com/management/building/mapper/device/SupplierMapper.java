package com.management.building.mapper.device;

import org.mapstruct.Mapper;

import com.management.building.dto.request.device.SupplierCreateRequest;
import com.management.building.dto.response.device.SupplierResponse;
import com.management.building.entity.device.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    Supplier toSupplierFromCreateRequest(SupplierCreateRequest createRequest);

    SupplierResponse toReponseFromCSupplier(Supplier supplier);
}
