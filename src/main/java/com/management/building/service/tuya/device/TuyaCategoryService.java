package com.management.building.service.tuya.device;

import java.util.List;

import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaCategoryDetail;

public interface TuyaCategoryService {
    TuyaReponse<List<TuyaCategoryDetail>> getCategoryList();

    void saveBulkCategoryList();
}
