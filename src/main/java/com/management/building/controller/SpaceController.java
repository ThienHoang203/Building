package com.management.building.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.SpaceReponse;
import com.management.building.service.SpaceService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/spaces")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SpaceController {
    SpaceService spaceService;

    @GetMapping("")
    public ApiResponse<List<SpaceReponse>> getAll() {

        var data = spaceService.getAll(false);

        return ApiResponse
                .<List<SpaceReponse>>builder()
                .code(200)
                .data(data)
                .build();
    }

    // @GetMapping("/{id}")
    // public ApiResponse<List<Object[]>> getAllDescendants(String id) {

    // var data = spaceService.getAllDescendants(id);

    // return ApiResponse
    // .<List<Object[]>>builder()
    // .code(200)
    // .data(data)
    // .build();
    // }
}
