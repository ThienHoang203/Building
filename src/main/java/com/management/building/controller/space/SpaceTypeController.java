package com.management.building.controller.space;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.service.space.SpaceTypeServiceImplement;

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
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Validated
@RequestMapping("/space-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceTypeController {
    SpaceTypeServiceImplement spaceTypeService;

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

    @GetMapping
    public ApiResponse<List<SpaceTypeResponse>> search(
            @RequestParam(defaultValue = "10", name = "pageSize") String pageSize,
            @RequestParam(defaultValue = "1", name = "pageNumber") String pageNumber,
            @RequestParam(defaultValue = "unsorted", name = "sortType") String sortType) {

        List<SpaceTypeResponse> reponseData = spaceTypeService.getSpaceTypes();

        return ApiResponse.<List<SpaceTypeResponse>>builder().code(200).data(reponseData).build();
    }

    @GetMapping("/{name}")
    public ApiResponse<SpaceTypeResponse> getByName(
            @PathVariable @NotBlank String name) {
        SpaceTypeResponse response = spaceTypeService.getSpaceTypeByName(name);
        return ApiResponse
                .<SpaceTypeResponse>builder()
                .code(200)
                .data(response)
                .build();
    }

    @GetMapping("/{name}/spaces")
    public ApiResponse<SpaceTypeResponse> getByNameWithSpaces(
            @PathVariable @NotBlank String name) {
        SpaceTypeResponse response = spaceTypeService.getSpaceTypeByName(name);
        return ApiResponse
                .<SpaceTypeResponse>builder()
                .code(200)
                .data(response)
                .build();
    }

    @DeleteMapping("/{name}")
    public ApiResponse<SpaceTypeResponse> delete(@PathVariable @NotBlank String name) {
        spaceTypeService.delete(name);

        return ApiResponse.<SpaceTypeResponse>builder().code(200)
                .message(String.format("The %s has been deleted successfully", name)).build();
    }
}
