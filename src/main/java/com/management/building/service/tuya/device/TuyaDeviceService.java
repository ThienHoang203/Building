package com.management.building.service.tuya.device;

import java.util.List;

import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaDeviceDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaLogResult;

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

}
