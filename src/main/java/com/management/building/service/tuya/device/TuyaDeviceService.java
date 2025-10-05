package com.management.building.service.tuya.device;

import java.util.List;

import com.management.building.dto.response.device.DeviceResponse;
import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaDeviceDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaLogResult;
import com.management.building.dto.response.tuyaCloud.device.TuyaPropertyResult;
import com.management.building.dto.response.tuyaCloud.device.TuyaStatusOfMulDevicesResponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaStatusResponse;

public interface TuyaDeviceService {
        TuyaReponse<List<TuyaDeviceDetail>> getDivicesInProject(
                        String productId,
                        String categories,
                        String lastId,
                        Integer page_size);

        TuyaReponse<TuyaLogResult> getLogByDeviceId(
                        String id,
                        String codes,
                        Long startTime,
                        Long endTime,
                        String lastRowKey,
                        Integer size);

        TuyaReponse<List<TuyaStatusResponse>> getStatusOfASingleDevice(String id);

        TuyaReponse<TuyaPropertyResult> getPropertiesOfDevice(String deviceId);

        TuyaReponse<TuyaDeviceDetail> getADevice(String id);

        TuyaReponse<List<TuyaStatusOfMulDevicesResponse>> getStatusOfMulDevices(String deviceIds);

        List<DeviceResponse> saveDevicesInAProject();

}
