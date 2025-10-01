package com.management.building.service.tuya.device;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaCategoryDetail;
import com.management.building.entity.device.Category;
import com.management.building.mapper.tuya.TuyaMapper;
import com.management.building.repository.device.CategoryRepository;
import com.management.building.service.tuya.TuyaApiClientImplement;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TuyaCategoryServiceImplement implements TuyaCategoryService {

    TuyaApiClientImplement apiClient;
    CategoryRepository categoryRepository;
    TuyaMapper tuyaMapper;

    @Override
    public TuyaReponse<List<TuyaCategoryDetail>> getCategoryList() {
        String url = "/v1.0/iot-03/device-categories";
        return apiClient.get(url, new ParameterizedTypeReference<TuyaReponse<List<TuyaCategoryDetail>>>() {
        });
    }

    @Override
    public void saveBulkCategoryList() {
        var response = getCategoryList();
        List<Category> categories = response.getResult().stream().map(tuyaMapper::toCategoryFromTuyaCategoryDetail)
                .toList();
        try {
            categoryRepository.saveAll(categories);
        } catch (Exception e) {
            throw new RuntimeException("bulk failed, cause: " + e.getMessage());
        }

    }

}
