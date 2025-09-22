package com.management.building.controller.space;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/spaces")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Validated
public class SpaceController {
    // SpaceServiceImplement spaceService;

    // @GetMapping
    // public ApiResponse<List<SpaceReponse>> getAll() {

    //     var data = spaceService.getAll();

    //     return ApiResponse
    //             .<List<SpaceReponse>>builder()
    //             .code(200)
    //             .data(data)
    //             .build();
    // }

    // @GetMapping("/{id}")
    // public ApiResponse<SpaceReponse> getByid(@PathVariable @NotNull @Min(value = 1) Long id) {

    //     SpaceReponse data = spaceService.getById(id);

    //     return ApiResponse
    //             .<SpaceReponse>builder()
    //             .code(200)
    //             .data(data)
    //             .build();
    // }

    // @DeleteMapping("/{id}")
    // public ApiResponse<SpaceReponse> deleteByid(@PathVariable @NotNull @Min(value = 1) Long id) {

    //     spaceService.delete(id);

    //     return ApiResponse
    //             .<SpaceReponse>builder()
    //             .code(200)
    //             .message(String.format("Space ID = %d has been deleted successfully", id))
    //             .build();
    // }

    // @PostMapping
    // @ResponseStatus(HttpStatus.CREATED)
    // public ApiResponse<SpaceReponse> create(@RequestBody @Valid SpaceCreateRequest requestBody) {

    //     SpaceReponse result = spaceService.create(requestBody);

    //     return ApiResponse
    //             .<SpaceReponse>builder()
    //             .code(201)
    //             .data(result)
    //             .build();
    // }

    // @PutMapping("/{id}")
    // public ApiResponse<SpaceReponse> update(@PathVariable @NotNull @Min(value = 1) Long id,
    //         @RequestBody @Valid SpaceUpdateRequest requestBody) {

    //     SpaceReponse result = spaceService.update(id, requestBody);

    //     return ApiResponse
    //             .<SpaceReponse>builder()
    //             .code(201)
    //             .data(result)
    //             .build();
    // }

    // @GetMapping("/{id}/childrens")
    // public ResponseEntity<SpacePaginationResponse<?>> getChildSpaces(
    //         @PathVariable Long id,
    //         @RequestParam(defaultValue = "flat") String format,
    //         @RequestParam(required = false) String cursor,
    //         @RequestParam(required = false) @Min(1) @Max(500) Integer limit,
    //         @RequestParam(required = false) @Min(1) @Max(20) Integer maxDepth,
    //         @RequestParam(defaultValue = "DESC") @Min(1) @Max(20) String sortOrder,
    //         @RequestParam(defaultValue = "false") Boolean lazy) {

    //     if ("nested".equals(format)) {
    //         SpacePaginationResponse<SpaceTreeResponse> response = spaceService
    //                 .getChildSpacesNested(id, maxDepth, lazy, sortOrder.toUpperCase());
    //         return ResponseEntity.ok(response);
    //     } else {
    //         SpacePaginationResponse<SpaceFlatResponse> response = spaceService
    //                 .getChildSpacesFlat(id, cursor, limit, maxDepth, sortOrder.toUpperCase());
    //         return ResponseEntity.ok(response);
    //     }
    // }

    // @GetMapping("/{id}/parents")
    // public ResponseEntity<SpacePaginationResponse<SpaceFlatResponse>> getParentSpaces(
    //         @PathVariable Long id,
    //         @RequestParam(required = false) @Min(1) @Max(20) Integer maxDepth) {

    //     SpacePaginationResponse<SpaceFlatResponse> response = spaceService.getParentSpaces(id, maxDepth);
    //     return ResponseEntity.ok(response);
    // }
}
