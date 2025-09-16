package com.management.building.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.SpaceTypeCreateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.SpaceTypeResponse;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.service.SpaceTypeService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/space-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceTypeController {
    SpaceTypeService spaceTypeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SpaceTypeResponse> create(@RequestBody @Valid SpaceTypeCreateRequest requestBody) {

        if (spaceTypeService.existsByName(requestBody.getName())) {
            throw new AppException(ErrorCode.SPACE_TYPE_NAME_EXISTS);
        }

        var data = spaceTypeService.create(requestBody);

        return ApiResponse
                .<SpaceTypeResponse>builder()
                .code(201)
                .data(data)
                .build();
    }

    @GetMapping
    public ApiResponse<List<SpaceTypeResponse>> getSpaceType() {

        List<SpaceTypeResponse> reponseData = spaceTypeService.getSpaceTypes();

        return ApiResponse.<List<SpaceTypeResponse>>builder()
                .code(200)
                .data(reponseData)
                .build();
    }

}
