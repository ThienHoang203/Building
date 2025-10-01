package com.management.building.service.tuya.device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaDeviceDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaLogResult;
import com.management.building.service.tuya.TuyaApiClientImplement;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TuyaDeviceServiceImplement implements TuyaDeviceService {
    TuyaApiClientImplement apiClient;

    @Override
    public TuyaReponse<List<TuyaDeviceDetail>> getDivicesInProject(
            String productId,
            String categories,
            String lastId,
            Integer pageSize) {
        try {
            // Build query parameters
            Map<String, String> queryParams = new HashMap<>();

            if (StringUtils.isBlank(productId)) {
                queryParams.put("product_ids", productId);
            }

            if (StringUtils.isBlank(categories)) {
                queryParams.put("categories", categories);
            }

            if (StringUtils.isBlank(lastId)) {
                queryParams.put("last_id", lastId);
            }

            queryParams.put("page_size", pageSize.toString());

            String url = "/v2.0/cloud/thing/device";

            TuyaReponse<List<TuyaDeviceDetail>> response = apiClient.get(
                    url,
                    new ParameterizedTypeReference<TuyaReponse<List<TuyaDeviceDetail>>>() {
                    },
                    queryParams);

            if (response != null && response.getSuccess()) {
                log.info("Successfully retrieved {} devices",
                        response.getResult() != null ? response.getResult().size() : 0);
            } else {
                log.warn("Failed to get devices: {}",
                        response != null ? response.getMsg() : "Unknown error");
            }

            return response;

        } catch (Exception e) {
            log.error("Error getting devices in project", e);
            throw new RuntimeException("Error getting devices in project", e);
        }
    }

    @Override
    public TuyaReponse<TuyaLogResult> getLogByDeviceId(String id, String codes, Long startTime, Long endTime,
            String lastRowKey,
            Integer size) {
        try {
            // Build query parameters
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("codes", codes);
            queryParams.put("start_time", startTime.toString());
            queryParams.put("last_row_key", lastRowKey == null ? "" : lastRowKey);
            queryParams.put("end_time", endTime.toString());
            queryParams.put("size", size.toString());

            String url = String.format("/v2.0/cloud/thing/%s/report-logs", id);

            TuyaReponse<TuyaLogResult> response = apiClient.get(
                    url,
                    new ParameterizedTypeReference<TuyaReponse<TuyaLogResult>>() {
                    },
                    queryParams);

            if (response != null && response.getSuccess()) {
                log.info("Successfully retrieved {} devices",
                        response.getResult() != null ? response.getResult().getDevice_id() : 0);
            } else {
                log.warn("Failed to get devices: {}",
                        response != null ? response.getMsg() : "Unknown error");
            }

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error getting devices in project", e);
        }
    }

}
