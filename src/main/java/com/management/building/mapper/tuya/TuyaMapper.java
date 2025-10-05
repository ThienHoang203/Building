package com.management.building.mapper.tuya;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.management.building.dto.response.tuyaCloud.device.TuyaCategoryDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaDeviceDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaLogDetail;
import com.management.building.entity.device.Category;
import com.management.building.entity.device.Device;
import com.management.building.entity.device.DeviceStatusLog;

@Mapper(componentModel = "spring")
public interface TuyaMapper {
    Category toCategoryFromTuyaCategoryDetail(TuyaCategoryDetail categoryDetail);

    @Mapping(target = "category", ignore = true)
    Device toDeviceFromTuyaDeviceDetail(TuyaDeviceDetail deviceDetail);

    @Mapping(target = "value", expression = "java(logDetail.getValue() != null ? String.valueOf(logDetail.getValue()) : \"\")")
    @Mapping(target = "statusCode", source = "code")
    DeviceStatusLog toDeviceStatusLogFromLogDetail(TuyaLogDetail logDetail);
}
