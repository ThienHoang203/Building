package com.management.building.service.tuya.device;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.management.building.dto.response.tuyaCloud.device.TuyaLogDetail;
import com.management.building.entity.device.Category;
import com.management.building.entity.device.Device;
import com.management.building.entity.device.DeviceStatusLog;
import com.management.building.mapper.tuya.TuyaMapper;
import com.management.building.repository.device.CategoryRepository;
import com.management.building.repository.device.DeviceRepository;
import com.management.building.repository.device.DeviceStatusLogRepository;
import com.management.building.repository.device.DeviceStatusRepository;
import com.management.building.service.tuya.TuyaApiClient;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TuyaLogSchedulerImplement implements TuyaLogScheduler {
    TuyaApiClient apiClient;
    DeviceStatusLogRepository statusHistoryRepository;
    DeviceStatusRepository statusRepository;
    DeviceRepository deviceRepository;
    CategoryRepository categoryRepository;
    EntityManager entityManager;
    TuyaDeviceService tuyaDeviceService;
    TuyaMapper tuyaMapper;

    @NonFinal
    @Value("${tuya..scheduler..time}")
    String timeString;

    @Override
    @Transactional
    @Scheduled(fixedRateString = "${tuya.scheduler.time}")
    public void syncDeviceStatusLog() {
        String batchId = UUID.randomUUID().toString();
        LocalDateTime recordedAt = LocalDateTime.now();

        List<Device> devices = deviceRepository.findAll();
        // get set of category with status response
        var mappedCategories = getMappedCategories(getSetOfCategoryCodes(devices));
        List<DeviceStatusLog> allLogs = new ArrayList<>();

        for (Device device : devices) {
            try {
                if (device.getCategory() == null)
                    continue;
                List<DeviceStatusLog> logs = fetchLogsFromTuya(device, mappedCategories, batchId, recordedAt);
                allLogs.addAll(logs);
            } catch (Exception e) {
                log.error("Failed to sync logs for device {}, cause  {}", device.getId(), e.getMessage());
            }
        }

        // Batch insert với JDBC batch
        batchInsertLogs(allLogs);

        log.info("Synced {} logs for {} devices", allLogs.size(), devices.size());
    }

    private Set<String> getSetOfCategoryCodes(List<Device> devices) {
        Set<String> categoryCodes = new HashSet<>();
        for (Device d : devices) {
            if (d.getCategory() != null) {
                categoryCodes.add(d.getCategory().getCode());
            }
        }
        return categoryCodes;
    }

    private Map<String, String> getMappedCategories(Set<String> categoryCodes) {
        var filteredCategory = categoryRepository.findByCodeIn(categoryCodes);
        if (CollectionUtils.isEmpty(filteredCategory)) {
            throw new RuntimeException("Error when get status codes");
        }
        Map<String, String> result = new HashMap<>(filteredCategory.size());
        for (Category category : filteredCategory) {
            String key = category.getCode();
            String value = category.getCategoryStatus()
                    .stream()
                    .map(e -> e.getCode())
                    .collect(Collectors.joining(","));
            result.put(key, value);
        }
        return result;
    }

    private List<DeviceStatusLog> fetchLogsFromTuya(
            Device device,
            Map<String, String> statusStringQuery,
            String batchId,
            LocalDateTime recordedAt) {
        var codeQuery = statusStringQuery.getOrDefault(device.getCategory().getCode(), null);
        if (codeQuery == null) {
            throw new RuntimeException("field to fetch logs from tuya, cause code query is null");
        }
        long startTime = device.getLastSyncTime() != null // Lấy từ lastSyncTime đến hiện tại
                ? device.getLastSyncTime()
                : System.currentTimeMillis() - Long.valueOf(timeString);
        long endTime = System.currentTimeMillis();
        var logResponse = tuyaDeviceService.getLogByDeviceId(device.getId(), codeQuery, startTime, endTime, null, 1);
        var logs = logResponse.getResult().getLogs();
        if (logs == null) {
            throw new RuntimeException("Failed to fetch log from tuya, cause logs is null");
        }
        List<DeviceStatusLog> resultLogs = new ArrayList<>(logs.size());
        for (TuyaLogDetail log : logs) {
            if (log != null) {
                var logEntity = tuyaMapper.toDeviceStatusLogFromLogDetail(log);
                logEntity.setBatchId(batchId);
                logEntity.setDevice(device);
                logEntity.setRecordedAt(recordedAt);
                resultLogs.add(logEntity);
            }
        }

        device.setLastSyncTime(endTime);
        deviceRepository.save(device);

        return resultLogs;
    }

    @Transactional
    public void batchInsertLogs(List<DeviceStatusLog> logs) {
        int batchSize = 500;

        for (int i = 0; i < logs.size(); i++) {
            entityManager.persist(logs.get(i));

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }

}
