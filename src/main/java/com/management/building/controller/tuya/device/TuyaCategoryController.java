package com.management.building.controller.tuya.device;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.response.app.ApiResponse;
import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.device.TuyaCategoryDetail;
import com.management.building.service.tuya.device.TuyaCategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@Validated
@RequestMapping("/tuya-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TuyaCategoryController {

    TuyaCategoryService categoryService;

    @GetMapping
    public TuyaReponse<List<TuyaCategoryDetail>> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @GetMapping("save-bulk")
    public ApiResponse<Void> saveBulkCategories() {
        categoryService.saveBulkCategoryList();
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Bulk insert successfully")
                .build();
    }

}
