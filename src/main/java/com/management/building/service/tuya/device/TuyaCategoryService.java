package com.management.building.service.tuya.device;

import java.util.List;

import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaCategoryDetail;
import com.management.building.dto.response.tuyaCloud.device.TuyaStatusSetResult;

public interface TuyaCategoryService {
    TuyaReponse<List<TuyaCategoryDetail>> getCategoryList();

    TuyaReponse<TuyaStatusSetResult> getStatusSetByCategoryCode(String code);

    void saveBulkCategoryList();
}
