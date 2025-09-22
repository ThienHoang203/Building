package com.management.building.controller.space;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateWithSpacesRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.space.SpaceTypeHierarchyResponse;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeWithSpacesResponse;
import com.management.building.enums.space.LoadingHierarchyMode;
import com.management.building.enums.space.UpdateListSpacesMode;
import com.management.building.enums.space.UpdateParentMode;
import com.management.building.service.space.SpaceTypeService;
import com.management.building.validators.ValidEnum;

import jakarta.validation.Valid;
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
        var data = spaceTypeService.create(requestBody);
        return ApiResponse.<SpaceTypeResponse>builder().code(201).data(data).build();
    }

    @PutMapping("/{name}")
    public ApiResponse<SpaceTypeResponse> update(@PathVariable @NotBlank String name,
            @RequestBody @Valid SpaceTypeUpdateRequest requestBody) {
        var result = spaceTypeService.update(name, requestBody);

        return ApiResponse.<SpaceTypeResponse>builder().code(200).data(result).build();
    }

    @GetMapping("/{name}/parents")
    public ApiResponse<List<SpaceTypeHierarchyResponse>> getByNameWithParent(@PathVariable @NotBlank String name,
            @RequestParam(defaultValue = "IMMEDIATE") @ValidEnum(enumClass = LoadingHierarchyMode.class) LoadingHierarchyMode mode) {
        List<SpaceTypeHierarchyResponse> result = spaceTypeService.getParents(name, mode);
        return ApiResponse.<List<SpaceTypeHierarchyResponse>>builder().code(200).data(result).build();
    }

    @GetMapping("/{name}/children")
    public ApiResponse<List<SpaceTypeHierarchyResponse>> getByNameWithChildren(@PathVariable @NotBlank String name) {
        List<SpaceTypeHierarchyResponse> result = spaceTypeService.getChildren(name);
        return ApiResponse.<List<SpaceTypeHierarchyResponse>>builder().code(200).data(result).build();
    }

    @GetMapping("/{name}/spaces")
    public ApiResponse<SpaceTypeWithSpacesResponse> getByNameWithSpaces(@PathVariable @NotBlank String name) {
        SpaceTypeWithSpacesResponse result = spaceTypeService.getByNameWithSpaces(name);
        return ApiResponse.<SpaceTypeWithSpacesResponse>builder().code(200).data(result).build();
    }

    @PatchMapping("/{name}/spaces")
    public ApiResponse<SpaceTypeWithSpacesResponse> updateWithSpaces(@PathVariable @NotBlank String name,
            @RequestParam(defaultValue = "ADD") @ValidEnum(enumClass = UpdateListSpacesMode.class, allowNull = true) String mode,
            @RequestBody SpaceTypeUpdateWithSpacesRequest requestBody) {
        SpaceTypeWithSpacesResponse result = spaceTypeService.updateWithSpaces(name, UpdateListSpacesMode.valueOf(mode),
                requestBody);
        return ApiResponse.<SpaceTypeWithSpacesResponse>builder().code(200).data(result).build();
    }

    @PatchMapping("/{name}/parent/{parentName}")
    public ApiResponse<SpaceTypeResponse> changeParent(@PathVariable @NotBlank String name,
            @PathVariable @NotBlank String parentName,
            @RequestParam(defaultValue = "NEW") @ValidEnum(enumClass = UpdateParentMode.class) String mode) {
        var result = spaceTypeService.updateParent(name, parentName, UpdateParentMode.valueOf(mode));
        return ApiResponse.<SpaceTypeResponse>builder().code(200).data(result).build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<SpaceTypeResponse> delete(@PathVariable @NotBlank String name) {
        spaceTypeService.delete(name);

        return ApiResponse.<SpaceTypeResponse>builder().code(200)
                .message(String.format("The %s has been deleted successfully", name)).build();
    }
}
