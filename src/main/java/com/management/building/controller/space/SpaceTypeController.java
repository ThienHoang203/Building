package com.management.building.controller.space;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeTreeRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeTreeResponse;
import com.management.building.service.space.SpaceTypeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Validated
@RequestMapping("/space-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceTypeController {
    SpaceTypeService spaceTypeService;

    @GetMapping
    public ApiResponse<List<SpaceTypeResponse>> getAll() {
        var data = spaceTypeService.getAll();
        return ApiResponse.<List<SpaceTypeResponse>>builder().code(200).data(data).build();
    }

    @GetMapping("/{name}")
    public ApiResponse<SpaceTypeResponse> getByName(@PathVariable @NotBlank String name) {
        SpaceTypeResponse response = spaceTypeService.getByName(name);
        return ApiResponse
                .<SpaceTypeResponse>builder()
                .code(200)
                .data(response)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SpaceTypeResponse> create(@RequestBody @Valid SpaceTypeCreateRequest requestBody) {
        var result = spaceTypeService.create(requestBody);
        return ApiResponse.<SpaceTypeResponse>builder().code(201).data(result).build();
    }

    @PutMapping("/{name}")
    public ApiResponse<SpaceTypeResponse> update(@PathVariable @NotBlank String name,
            @RequestBody @Valid SpaceTypeUpdateRequest requestBody) {
        var result = spaceTypeService.update(name, requestBody);

        return ApiResponse.<SpaceTypeResponse>builder().code(200).data(result).build();
    }

    @PatchMapping("/{name}/{parentName}")
    public ApiResponse<SpaceTypeResponse> updateParent(@PathVariable @NotBlank String name,
            @PathVariable @NotBlank String parentName) {
        SpaceTypeResponse response = spaceTypeService.updateParent(name, parentName);
        return ApiResponse
                .<SpaceTypeResponse>builder()
                .code(200)
                .data(response)
                .build();
    }

    @GetMapping("/{name}/tree")
    public ApiResponse<SpaceTypeTreeResponse> getSpaceTypeTree(
            @PathVariable @NotBlank String name,
            @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer limitDepth) {

        SpaceTypeTreeRequest request = SpaceTypeTreeRequest.builder()
                .limitDepth(limitDepth)
                .build();
        SpaceTypeTreeResponse result = spaceTypeService.getSpaceTypeTree(name, request);
        return ApiResponse.<SpaceTypeTreeResponse>builder().code(200).data(result).build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<SpaceTypeResponse> delete(@PathVariable @NotBlank String name) {
        spaceTypeService.delete(name);

        return ApiResponse.<SpaceTypeResponse>builder().code(200)
                .message(String.format("The %s has been deleted successfully", name)).build();
    }
}
