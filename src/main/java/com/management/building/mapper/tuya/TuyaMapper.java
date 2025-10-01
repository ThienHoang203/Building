package com.management.building.mapper.tuya;

import org.mapstruct.Mapper;

import com.management.building.dto.response.tuyaCloud.device.TuyaCategoryDetail;
import com.management.building.entity.device.Category;

@Mapper(componentModel = "spring")
public interface TuyaMapper {
    Category toCategoryFromTuyaCategoryDetail(TuyaCategoryDetail categoryDetail);
}
