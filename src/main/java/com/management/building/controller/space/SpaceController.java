package com.management.building.controller.space;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.space.SpaceHierarchyResponse;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceWithChildrenResponse;
import com.management.building.enums.space.LoadingHierarchyMode;
import com.management.building.enums.space.SpaceStatus;
import com.management.building.enums.space.UpdateParentMode;
import com.management.building.service.space.SpaceService;
import com.management.building.validators.ValidEnum;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@Validated
@RequestMapping("/spaces")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceController {
    SpaceService spaceService;

    @GetMapping
    public ApiResponse<List<SpaceResponse>> getAll() {
        var data = spaceService.getAll();
        return ApiResponse.<List<SpaceResponse>>builder().code(200).data(data).build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SpaceResponse> getById(@PathVariable @NotNull Long id) {
        SpaceResponse response = spaceService.getById(id);
        return ApiResponse
                .<SpaceResponse>builder()
                .code(200)
                .data(response)
                .build();
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<SpaceResponse>> getByStatus(
            @PathVariable @ValidEnum(enumClass = SpaceStatus.class) String status) {
        List<SpaceResponse> data = spaceService.getByStatus(SpaceStatus.valueOf(status));
        return ApiResponse.<List<SpaceResponse>>builder().code(200).data(data).build();
    }

    @GetMapping("/type/{typeName}")
    public ApiResponse<List<SpaceResponse>> getByTypeName(@PathVariable String typeName) {
        List<SpaceResponse> data = spaceService.getByTypeName(typeName);
        return ApiResponse.<List<SpaceResponse>>builder().code(200).data(data).build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SpaceResponse> create(@RequestBody @Valid SpaceCreateRequest requestBody) {
        var data = spaceService.create(requestBody);
        return ApiResponse.<SpaceResponse>builder().code(201).data(data).build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SpaceResponse> update(@PathVariable @NotNull Long id,
            @RequestBody @Valid SpaceUpdateRequest requestBody) {
        var result = spaceService.update(id, requestBody);
        return ApiResponse.<SpaceResponse>builder().code(200).data(result).build();
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<SpaceResponse> updateStatus(@PathVariable @NotNull Long id,
            @RequestParam @ValidEnum(enumClass = SpaceStatus.class) String status) {
        var result = spaceService.updateStatus(id, SpaceStatus.valueOf(status));
        return ApiResponse.<SpaceResponse>builder().code(200).data(result).build();
    }

    @GetMapping("/{id}/parents")
    public ApiResponse<List<SpaceHierarchyResponse>> getParents(@PathVariable @NotNull Long id,
            @RequestParam(defaultValue = "IMMEDIATE") @ValidEnum(enumClass = LoadingHierarchyMode.class) String mode) {
        List<SpaceHierarchyResponse> result = spaceService.getParents(id, LoadingHierarchyMode.valueOf(mode));
        return ApiResponse.<List<SpaceHierarchyResponse>>builder().code(200).data(result).build();
    }

    @GetMapping("/{id}/children")
    public ApiResponse<SpaceWithChildrenResponse> getChildren(@PathVariable @NotNull Long id) {
        SpaceWithChildrenResponse result = spaceService.getChildren(id);
        return ApiResponse.<SpaceWithChildrenResponse>builder().code(200).data(result).build();
    }

    @PatchMapping("/{id}/parent/{parentId}")
    public ApiResponse<SpaceResponse> changeParent(@PathVariable @NotNull Long id,
            @PathVariable @NotNull Long parentId,
            @RequestParam(defaultValue = "NEW") @ValidEnum(enumClass = UpdateParentMode.class) String mode) {
        var result = spaceService.updateParent(id, parentId, UpdateParentMode.valueOf(mode));
        return ApiResponse.<SpaceResponse>builder().code(200).data(result).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable @NotNull Long id) {
        spaceService.delete(id);
        return ApiResponse.<Void>builder().code(200)
                .message(String.format("Space with id %d has been deleted successfully", id)).build();
    }
}