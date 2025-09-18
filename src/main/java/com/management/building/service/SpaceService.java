package com.management.building.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.management.building.dto.request.SpaceCreateRequest;
import com.management.building.dto.request.SpaceUpdateRequest;
import com.management.building.dto.response.SpaceFlatResponse;
import com.management.building.dto.response.SpacePaginationResponse;
import com.management.building.dto.response.SpaceReponse;
import com.management.building.dto.response.SpaceTreeResponse;
import com.management.building.entity.Space;
import com.management.building.entity.SpaceType;
import com.management.building.enums.SpaceStatus;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.SpaceMapper;
import com.management.building.repository.SpaceRepository;
import com.management.building.repository.SpaceTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceService {
    SpaceRepository spaceRepo;
    SpaceTypeRepository spaceTypeRepo;
    SpaceMapper spaceMapper;

    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int MAX_PAGE_SIZE = 500;
    private static final int DEFAULT_MAX_DEPTH = 10;

    public List<SpaceReponse> getAll(boolean isAllLoaded) {
        var result = spaceRepo.findAll();
        List<SpaceReponse> response = new ArrayList<>();
        for (Space space : result) {
            response.add(spaceMapper.toSpaceResponseFromSpace(space));
        }
        return response;
    }

    public SpaceReponse create(SpaceCreateRequest requestBody) {
        if (requestBody.getParentSpaceId() != null && !spaceRepo.existsById(requestBody.getParentSpaceId())) {
            throw new AppException(ErrorCode.PARENT_SPACE_NOT_FOUND);
        }

        // make space type name to uppercase
        requestBody.setSpaceTypeName(requestBody.getSpaceTypeName().toUpperCase());

        SpaceType spaceType = spaceTypeRepo.findById(requestBody.getSpaceTypeName())
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));

        // map to Space class from request
        Space space = spaceMapper.toSpaceFromSpaceCreateRequest(requestBody);

        // map remain fields
        space.setType(spaceType);

        // save the new space into DB
        Space result = spaceRepo.save(space);

        // map to SpaceResponse class from saving result
        SpaceReponse response = spaceMapper.toSpaceResponseFromSpace(result);
        response.setParentSpaceId(requestBody.getParentSpaceId());
        response.setSpaceTypeName(requestBody.getSpaceTypeName());
        return response;
    }

    public SpaceReponse getById(Long id) {
        Space result = spaceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));

        return spaceMapper.toSpaceResponseFromSpace(result);
    }

    public void delete(Long id) {
        if (!spaceRepo.existsById(id)) {
            throw new AppException(ErrorCode.SPACE_NOT_FOUND);
        }

        try {
            spaceRepo.deleteById(id);
        } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.DELETE_FAILED);
        } catch (DataIntegrityViolationException e) {
            Throwable throwable = e.getCause();
            if (throwable instanceof org.hibernate.exception.ConstraintViolationException cve) {
                var message = cve.getCause().getMessage();
                if (message != null) {
                    if (message.contains("REFERENCE constraint")) {
                        throw new AppException(ErrorCode.FOREIGN_KEY_VIOLATION);
                    }
                }
            }
            throw new RuntimeException("Lỗi tính toàn vẹn dữ liệu", e);
        }

    }

    public SpaceReponse update(Long id, SpaceUpdateRequest requestBody) {
        Space originalObject = spaceRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));

        // check if the parent id must be different from child id
        if (id == requestBody.getParentSpaceId()) {
            throw new AppException(ErrorCode.PARENT_SAME_CHILD_ID);
        }

        // make space type name to uppercase
        requestBody.setSpaceTypeName(requestBody.getSpaceTypeName().toUpperCase());

        // update data from request to original space
        spaceMapper.updateSpaceFromSpaceUpdateRequest(requestBody, originalObject);

        // check constraints exist
        Space parentSpaceRequest = null;
        SpaceType spaceTypeRequest = null;
        List<Space> childSpaceRequest = null;

        if (requestBody.getParentSpaceId() != null) {
            parentSpaceRequest = spaceRepo.findById(requestBody.getParentSpaceId())
                    .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));
        }

        if (!CollectionUtils.isEmpty(requestBody.getChildSpaceIds())) {
            if (requestBody.getChildSpaceIds().contains(null)) {
                throw new AppException(ErrorCode.COLLECTION_CONTAIN_NULL);
            }
            childSpaceRequest = spaceRepo.findAllById(requestBody.getChildSpaceIds());
        }

        if (requestBody.getSpaceTypeName() != null) {
            spaceTypeRequest = spaceTypeRepo.findById(requestBody.getSpaceTypeName())
                    .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
        }

        SpaceReponse response = spaceMapper.toSpaceResponseFromSpace(originalObject);

        if (parentSpaceRequest != null) {
            response.setParentSpaceId(requestBody.getParentSpaceId());
            // update parent space
            originalObject.setParentSpace(parentSpaceRequest);
        }

        if (spaceTypeRequest != null) {
            response.setSpaceTypeName(requestBody.getSpaceTypeName());
            // update space type
            originalObject.setType(spaceTypeRequest);
        }

        if (childSpaceRequest != null) {
            response.setChildSpaceIds(requestBody.getChildSpaceIds());
            // update child spaces
            originalObject.setChildSpaces(new HashSet<>(childSpaceRequest));

        }

        // save updated information
        spaceRepo.save(originalObject);

        return response;
    }

    public SpacePaginationResponse<SpaceFlatResponse> getDescendantsFlat(
            Long parentId,
            String cursor,
            Integer limit,
            Integer maxDepth) {

        limit = Math.min(limit != null ? limit : DEFAULT_PAGE_SIZE, MAX_PAGE_SIZE);
        maxDepth = Math.min(maxDepth != null ? maxDepth : DEFAULT_MAX_DEPTH, 20);

        // Check cache first
        // String cacheKey = String.format("descendants:flat:%d:%s:%d:%d", parentId,
        // cursor, limit, maxDepth);
        // SpaceHierarchyResponse<SpaceFlatDto> cached = getCachedResponse(cacheKey);
        // if (cached != null)
        // return cached;

        List<Object[]> rawData = spaceRepo.findDescendantsWithPagination(
                parentId, maxDepth, cursor, limit + 1);

        boolean hasMore = rawData.size() > limit;
        if (hasMore)
            rawData.remove(rawData.size() - 1);

        List<SpaceFlatResponse> descendants = rawData.stream()
                .map(spaceMapper::toSpaceFlatResponseFromObject)
                .collect(Collectors.toList());

        String nextCursor = hasMore ? generateCursor(rawData.get(rawData.size() - 1)) : null;

        SpacePaginationResponse<SpaceFlatResponse> response = SpacePaginationResponse.<SpaceFlatResponse>builder()
                .data(descendants)
                .pagination(SpacePaginationResponse.PaginationInfo.builder()
                        .nextCursor(nextCursor)
                        .hasMore(hasMore)
                        .build())
                .meta(SpacePaginationResponse.HierarchyMeta.builder()
                        .rootId(parentId)
                        .maxDepth(maxDepth)
                        .totalNodes(descendants.size())
                        .format("flat")
                        .build())
                .build();

        // Cache for 5 minutes
        // cacheResponse(cacheKey, response, 300);
        return response;
    }

    public SpacePaginationResponse<SpaceTreeResponse> getDescendantsNested(
            Long parentId,
            Integer maxDepth,
            Boolean lazy) {

        maxDepth = Math.min(maxDepth != null ? maxDepth : 3, 5); // Limit depth for nested

        // String cacheKey = String.format("descendants:nested:%d:%d:%s", parentId,
        // maxDepth, lazy);
        // SpaceHierarchyResponse<SpaceTreeDto> cached = getCachedResponse(cacheKey);
        // if (cached != null)
        // return cached;

        // Load all descendants in flat format first
        List<Object[]> rawData = spaceRepo.findDescendantsWithPagination(
                parentId, maxDepth, null, 10000); // Large limit for nested

        // Build nested tree
        List<SpaceTreeResponse> tree = buildNestedTree(rawData, lazy);

        SpacePaginationResponse<SpaceTreeResponse> response = SpacePaginationResponse.<SpaceTreeResponse>builder()
                .data(tree)
                .pagination(SpacePaginationResponse.PaginationInfo.builder()
                        .hasMore(false) // Nested format loads all at once
                        .build())
                .meta(SpacePaginationResponse.HierarchyMeta.builder()
                        .rootId(parentId)
                        .maxDepth(maxDepth)
                        .totalNodes(rawData.size())
                        .format("nested")
                        .build())
                .build();

        // cacheResponse(cacheKey, response, 600); // Cache longer for nested
        return response;
    }

    private List<SpaceTreeResponse> buildNestedTree(List<Object[]> flatData, Boolean lazy) {
        Map<Long, SpaceTreeResponse> nodeMap = new HashMap<>();
        Map<Long, List<SpaceTreeResponse>> childrenMap = new HashMap<>();

        // Build nodes and group children
        for (Object[] row : flatData) {
            SpaceTreeResponse node = SpaceTreeResponse.builder()
                    .id((Long) row[0])
                    .name((String) row[1])
                    .status(SpaceStatus.valueOf((String) row[2]))
                    .capacity((Integer) row[3])
                    .area((Double) row[4])
                    .children(new ArrayList<>())
                    .hasChildren(lazy) // Will be set properly below
                    .build();

            nodeMap.put(node.getId(), node);

            Long parentId = (Long) row[8];
            if (parentId != null) {
                childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(node);
            }
        }

        // Link children to parents
        for (Map.Entry<Long, List<SpaceTreeResponse>> entry : childrenMap.entrySet()) {
            SpaceTreeResponse parent = nodeMap.get(entry.getKey());
            if (parent != null) {
                parent.setChildren(entry.getValue());
                parent.setChildrenCount(entry.getValue().size());
            }
        }

        // Return root level nodes
        return childrenMap.values().stream()
                .flatMap(List::stream)
                .filter(node -> flatData.stream()
                        .anyMatch(row -> row[8] == null && row[0].equals(node.getId())))
                .collect(Collectors.toList());
    }

    private String generateCursor(Object[] row) {
        return String.format("%d:%s:%d", (Integer) row[9], (String) row[1], (Long) row[0]);
    }

    public SpacePaginationResponse<SpaceFlatResponse> getAncestors(Long spaceId, Integer maxDepth) {
        maxDepth = maxDepth != null ? maxDepth : DEFAULT_MAX_DEPTH;

        // String cacheKey = String.format("ancestors:%d:%d", spaceId, maxDepth);
        // SpacePaginationResponse<SpaceFlatResponse> cached =
        // getCachedResponse(cacheKey);
        // if (cached != null) return cached;

        List<Object[]> rawData = spaceRepo.findAncestors(spaceId, maxDepth);

        List<SpaceFlatResponse> ancestors = rawData.stream()
                .map(spaceMapper::toSpaceFlatResponseFromObject)
                .collect(Collectors.toList());

        SpacePaginationResponse<SpaceFlatResponse> response = SpacePaginationResponse.<SpaceFlatResponse>builder()
                .data(ancestors)
                .pagination(SpacePaginationResponse.PaginationInfo.builder()
                        .hasMore(false)
                        .build())
                .meta(SpacePaginationResponse.HierarchyMeta.builder()
                        .rootId(ancestors.isEmpty() ? null : ancestors.get(ancestors.size() - 1).getId())
                        .maxDepth(maxDepth)
                        .totalNodes(ancestors.size())
                        .format("flat")
                        .build())
                .build();

        // cacheResponse(cacheKey, response, 1800); // Cache longer for ancestors (30
        // min)
        return response;
    }
}
