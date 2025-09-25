package com.management.building.mapper.smartDevice;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.management.building.dto.request.smartDevice.SupplierCreateRequest;
import com.management.building.dto.request.smartDevice.SupplierUpdateRequest;
import com.management.building.dto.response.smartDevice.SupplierResponse;
import com.management.building.entity.smartDevice.Supplier;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    Supplier toSupplierFromCreateRequest(SupplierCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Supplier toSupplierFromUpdateRequest(SupplierUpdateRequest updateRequest);

    SupplierResponse toResponseFromSupplier(Supplier supplier);
}
