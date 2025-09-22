package com.management.building.service.space;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceHierarchyResponse;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceWithChildrenResponse;
import com.management.building.entity.space.Space;
import com.management.building.entity.space.SpaceType;
import com.management.building.enums.space.LoadingHierarchyMode;
import com.management.building.enums.space.SpaceStatus;
import com.management.building.enums.space.UpdateParentMode;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.space.SpaceMapper;
import com.management.building.repository.space.SpaceRepository;
import com.management.building.repository.space.SpaceTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SpaceServiceImplement implements SpaceService {
    SpaceRepository spaceRepo;
    SpaceTypeRepository spaceTypeRepo;
    SpaceMapper spaceMapper;

    @Override
    @Transactional
    public SpaceResponse create(SpaceCreateRequest request) {
        // Validate and normalize name
        String name = request.getName();
        if (!isValidString(name)) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        name = name.trim();
        request.setName(name);
        // Validate and get SpaceType
        String typeName = request.getTypeName();
        if (!isValidString(typeName)) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        typeName = typeName.trim().toUpperCase();
        SpaceType spaceType = spaceTypeRepo.findById(typeName)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
        // Validate parent if provided
        Space parent = null;
        if (request.getParentId() != null) {
            parent = spaceRepo.findById(request.getParentId())
                    .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_NOT_FOUND));
        }
        // Create entity
        Space entity = spaceMapper.toSpaceFromCreateRequest(request);
        entity.setType(spaceType);
        entity.setParent(parent);
        try {
            Space savedSpace = spaceRepo.save(entity);
            return spaceMapper.toResponseFromSpace(savedSpace);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SPACE_CREATE_FAILED);
        } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
        } catch (Exception e) {
            log.error("Unexpected error while creating Space", e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    public List<SpaceResponse> getAll() {
        List<Space> spaces = spaceRepo.findAll();
        return spaces.stream()
                .map(spaceMapper::toResponseFromSpace)
                .collect(Collectors.toList());
    }

    @Override
    public SpaceResponse getById(Long id) {
        Space space = spaceRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));
        return spaceMapper.toResponseFromSpace(space);
    }

    @Override
    public List<SpaceResponse> getByStatus(SpaceStatus status) {
        List<Space> spaces = spaceRepo.findByStatus(status);
        return spaces.stream()
                .map(spaceMapper::toResponseFromSpace)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpaceResponse> getByTypeName(String typeName) {
        if (!isValidString(typeName)) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        typeName = typeName.trim().toUpperCase();

        // Check if type exists
        if (!spaceTypeRepo.existsById(typeName)) {
            throw new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND);
        }

        List<Space> spaces = spaceRepo.findByTypeName(typeName);
        return spaces.stream()
                .map(spaceMapper::toResponseFromSpace)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SpaceResponse update(Long id, SpaceUpdateRequest request) {
        Space entity = spaceRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));

        // Validate name if provided
        if (request.getName() != null) {
            String newName = request.getName().trim();
            request.setName(newName);
        }

        // Validate and update type if provided
        if (request.getTypeName() != null) {
            String typeName = request.getTypeName().trim().toUpperCase();
            SpaceType newType = spaceTypeRepo.findById(typeName)
                    .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
            entity.setType(newType);
        }

        // Update other fields
        spaceMapper.updateSpaceFromUpdateRequest(request, entity);

        try {
            Space updatedSpace = spaceRepo.save(entity);
            return spaceMapper.toResponseFromSpace(updatedSpace);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SPACE_UPDATE_FAILED);
        } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
        } catch (Exception e) {
            log.error("Unexpected error while updating Space with id: {}", id, e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    @Transactional
    public SpaceResponse updateStatus(Long id, SpaceStatus status) {
        Space space = spaceRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));

        space.setStatus(status);

        try {
            Space updatedSpace = spaceRepo.save(space);
            return spaceMapper.toResponseFromSpace(updatedSpace);
        } catch (Exception e) {
            log.error("Unexpected error while updating Space status with id: {}", id, e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    @Transactional
    public SpaceResponse updateParent(Long id, Long parentId, UpdateParentMode mode) {
        if (id.equals(parentId)) {
            throw new AppException(ErrorCode.PARENT_HAS_SAME_CHILD_ID);
        }

        Space entity = spaceRepo.findByIdWithParentAndType(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));

        switch (mode) {
            case NULL:
                if (entity.getParent() != null) {
                    if (!entity.getParent().getId().equals(parentId)) {
                        throw new AppException(ErrorCode.SPACE_HAS_DIFFERENT_PARENT);
                    }
                } else {
                    throw new AppException(ErrorCode.SPACE_HAS_NO_PARENT);
                }
                entity.setParent(null);
                break;

            case NEW:
                if (entity.getParent() != null && entity.getParent().getId().equals(parentId)) {
                    throw new AppException(ErrorCode.SAME_OLD_PARENT);
                }

                Space newParent = spaceRepo.findById(parentId)
                        .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_NOT_FOUND));

                // Check for valid level
                if (entity.getType().getLevel() <= newParent.getType().getLevel()) {
                    throw new AppException(ErrorCode.INVALID_PARENT_LEVEL);
                }
                entity.setParent(newParent);
                break;
        }

        try {
            Space updatedSpace = spaceRepo.save(entity);
            return spaceMapper.toResponseFromSpace(updatedSpace);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SPACE_UPDATE_FAILED);
        } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
        } catch (Exception e) {
            log.error("Unexpected error while updating Space parent with id: {}", id, e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!spaceRepo.existsById(id)) {
            throw new AppException(ErrorCode.SPACE_NOT_FOUND);
        }

        // Check if space has children
        if (spaceRepo.countByParentId(id) > 0) {
            throw new AppException(ErrorCode.HAS_CHILDREN);
        }

        try {
            spaceRepo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SPACE_IN_USE);
        } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
        } catch (Exception e) {
            log.error("Unexpected error while deleting Space with id: {}", id, e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    public List<SpaceHierarchyResponse> getParents(Long id, LoadingHierarchyMode mode) {
        return switch (mode) {
            case IMMEDIATE -> getImmediateParent(id);
            case FULL_PATH -> getFullParentHierarchy(id);
        };
    }

    @Override
    public SpaceWithChildrenResponse getChildren(Long id) {
        Space space = spaceRepo.findByIdWithChildren(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));

        SpaceWithChildrenResponse response = spaceMapper.toResponseWithChildrenFromSpace(space);

        if (space.getChildren() != null && !space.getChildren().isEmpty()) {
            List<SpaceResponse> childResponses = space.getChildren().stream()
                    .map(spaceMapper::toResponseFromSpace)
                    .collect(Collectors.toList());
            response.setChildren(childResponses);
        } else {
            response.setChildren(new ArrayList<>());
        }

        return response;
    }

    @Override
    public List<SpaceResponse> getRootSpaces() {
        List<Space> rootSpaces = spaceRepo.findRootSpaces();
        return rootSpaces.stream()
                .map(spaceMapper::toResponseFromSpace)
                .collect(Collectors.toList());
    }

    // Private helper methods
    private boolean isValidString(String str) {
        return str != null && StringUtils.hasText(str);
    }

    private List<SpaceHierarchyResponse> getImmediateParent(Long id) {
        Space space = spaceRepo.findByIdWithParent(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));

        List<SpaceHierarchyResponse> response = new ArrayList<>();
        response.add(spaceMapper.toHierarchyResponseFromSpace(space));

        if (space.getParent() != null) {
            response.add(spaceMapper.toHierarchyResponseFromSpace(space.getParent()));
        }

        return response;
    }

    private List<SpaceHierarchyResponse> getFullParentHierarchy(Long id) {
        List<Object[]> hierarchy = spaceRepo.findParentHierarchy(id);
        return hierarchy.stream()
                .map(spaceMapper::toHierarchyResponseFromObject)
                .collect(Collectors.toList());
    }
}