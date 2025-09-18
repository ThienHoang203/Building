package com.management.building.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.request.SpaceCreateRequest;
import com.management.building.dto.request.SpaceUpdateRequest;
import com.management.building.dto.response.ApiResponse;
import com.management.building.dto.response.SpaceFlatResponse;
import com.management.building.dto.response.SpacePaginationResponse;
import com.management.building.dto.response.SpaceReponse;
import com.management.building.dto.response.SpaceTreeResponse;
import com.management.building.service.SpaceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/spaces")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SpaceController {
    SpaceService spaceService;

    @GetMapping
    public ApiResponse<List<SpaceReponse>> getAll() {

        var data = spaceService.getAll(false);

        return ApiResponse
                .<List<SpaceReponse>>builder()
                .code(200)
                .data(data)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<SpaceReponse> getByid(@PathVariable @NotNull @Min(value = 1) Long id) {

        SpaceReponse data = spaceService.getById(id);

        return ApiResponse
                .<SpaceReponse>builder()
                .code(200)
                .data(data)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<SpaceReponse> deleteByid(@PathVariable @NotNull @Min(value = 1) Long id) {

        spaceService.delete(id);

        return ApiResponse
                .<SpaceReponse>builder()
                .code(200)
                .message(String.format("Space ID = %d has been deleted successfully", id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SpaceReponse> create(@RequestBody @Valid SpaceCreateRequest requestBody) {

        SpaceReponse result = spaceService.create(requestBody);

        return ApiResponse
                .<SpaceReponse>builder()
                .code(201)
                .data(result)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SpaceReponse> update(@PathVariable @NotNull @Min(value = 1) Long id,
            @RequestBody @Valid SpaceUpdateRequest requestBody) {

        SpaceReponse result = spaceService.update(id, requestBody);

        return ApiResponse
                .<SpaceReponse>builder()
                .code(201)
                .data(result)
                .build();
    }

    @GetMapping("/{id}/descendants")
    public ResponseEntity<SpacePaginationResponse<?>> getDescendants(
            @PathVariable Long id,
            @RequestParam(defaultValue = "flat") String format,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false) @Min(1) @Max(500) Integer limit,
            @RequestParam(required = false) @Min(1) @Max(20) Integer maxDepth,
            @RequestParam(defaultValue = "false") Boolean lazy) {

        if ("nested".equals(format)) {
            SpacePaginationResponse<SpaceTreeResponse> response = spaceService
                    .getDescendantsNested(id, maxDepth, lazy);
            return ResponseEntity.ok(response);
        } else {
            SpacePaginationResponse<SpaceFlatResponse> response = spaceService
                    .getDescendantsFlat(id, cursor, limit, maxDepth);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{id}/ancestors")
    public ResponseEntity<SpacePaginationResponse<SpaceFlatResponse>> getAncestors(
            @PathVariable Long id,
            @RequestParam(required = false) @Min(1) @Max(20) Integer maxDepth) {

        SpacePaginationResponse<SpaceFlatResponse> response = spaceService.getAncestors(id, maxDepth);
        return ResponseEntity.ok(response);
    }
}
