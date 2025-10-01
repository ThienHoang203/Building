package com.management.building.controller.space;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceTreeRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.app.ApiResponse;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceTreeResponse;
import com.management.building.service.space.SpaceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

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

        @GetMapping("/{id}/tree")
        public ApiResponse<SpaceTreeResponse> getTreeById(@PathVariable @NotNull @DecimalMin(value = "1.0") Long id,
                        @RequestParam(defaultValue = "10") @Min(1) @Max(50) Integer limitDepth) {
                SpaceTreeRequest request = SpaceTreeRequest.builder()
                                .limitDepth(limitDepth)
                                .build();
                SpaceTreeResponse response = spaceService.getSpaceTree(id, request);
                return ApiResponse
                                .<SpaceTreeResponse>builder()
                                .code(200)
                                .data(response)
                                .build();
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<SpaceResponse> create(@RequestBody @Valid SpaceCreateRequest requestBody) {
                var result = spaceService.create(requestBody);
                return ApiResponse
                                .<SpaceResponse>builder()
                                .code(200)
                                .data(result)
                                .build();
        }

        @PutMapping("/{id}")
        public ApiResponse<SpaceResponse> update(@PathVariable @DecimalMin(value = "1.0", inclusive = true) Long id,
                        @RequestBody @Valid SpaceUpdateRequest requestBody) {
                var result = spaceService.update(id, requestBody);
                return ApiResponse
                                .<SpaceResponse>builder()
                                .code(200)
                                .data(result)
                                .build();
        }

        @PatchMapping("/{id}/{parentId}")
        public ApiResponse<SpaceResponse> updateParent(
                        @PathVariable @DecimalMin(value = "1.0", inclusive = true) Long id,
                        @PathVariable @DecimalMin(value = "1.0", inclusive = true) Long parentId) {
                var result = spaceService.updateParent(id, parentId);
                return ApiResponse
                                .<SpaceResponse>builder()
                                .code(200)
                                .data(result)
                                .build();
        }

        @DeleteMapping("/{id}")
        public ApiResponse<Void> delete(@PathVariable @DecimalMin(value = "1.0", inclusive = true) Long id) {
                spaceService.delete(id);
                return ApiResponse
                                .<Void>builder()
                                .code(200)
                                .message("Delete successsfully")
                                .build();
        }
}