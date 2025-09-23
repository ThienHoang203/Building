package com.management.building.service.space;

import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.management.building.dto.request.space.SpaceCreateRequest;
import com.management.building.dto.request.space.SpaceTreeRequest;
import com.management.building.dto.request.space.SpaceUpdateRequest;
import com.management.building.dto.response.space.SpaceRaw;
import com.management.building.dto.response.space.SpaceResponse;
import com.management.building.dto.response.space.SpaceTreeResponse;
import com.management.building.entity.space.Space;
import com.management.building.entity.space.SpaceType;
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
        Long parentId = request.getParentId();
        String typeName = request.getTypeName().trim().toUpperCase();
        SpaceType type = spaceTypeRepo.findById(typeName)
                .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
        Space parent = null;
        int parentLevel = type.getLevel() - 1;
        if (parentId != null) {
            parent = spaceRepo.findByIdAndLevel(parentId, parentLevel)
                    .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_IN_LEVEL_NOT_FOUND));
        }

        var entity = spaceMapper.toSpaceFromCreateRequest(request);
        entity.setParent(parent);
        entity.setType(type);
        var result = spaceRepo.save(entity);
        return spaceMapper.toResponseFromSpace(result);
    }

    @Override
    public List<SpaceResponse> getAll() {
        var result = spaceRepo.findAll();
        return result.stream().map(spaceMapper::toResponseFromSpace).toList();
    }

    @Override
    public SpaceResponse getById(Long id) {
        var result = spaceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));
        return spaceMapper.toResponseFromSpace(result);
    }

    @Override
    public SpaceResponse update(Long id, SpaceUpdateRequest request) {
        Space entity = spaceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));
        spaceMapper.updateSpaceFromUpdateRequest(request, entity);
        var result = spaceRepo.save(entity);
        return spaceMapper.toResponseFromSpace(result);
    }

    @Override
    public SpaceResponse updateParent(Long id, Long parentId) {
        if (id == parentId) {
            throw new AppException(ErrorCode.PARENT_HAS_SAME_CHILD_ID);
        }
        var entity = spaceRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.SPACE_NOT_FOUND));
        int parentLevel = entity.getType().getLevel() - 1;
        var parent = spaceRepo.findByIdAndLevel(parentId, parentLevel)
                .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_TYPE_IN_LEVEL_NOT_FOUND));
        entity.setParent(parent);
        var result = spaceRepo.save(entity);
        return spaceMapper.toResponseFromSpace(result);
    }

    @Override
    public void delete(Long id) {
        if (!spaceRepo.existsById(id)) {
            throw new AppException(ErrorCode.SPACE_NOT_FOUND);
        }
        try {
            spaceRepo.deleteById(id);
        } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
        } catch (Exception e) {
            log.error("Unexpected error while deleting Space: {}", id, e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
        }
    }

    @Override
    public SpaceTreeResponse getSpaceTree(Long id, SpaceTreeRequest requestBody) {
        if (!spaceRepo.existsById(id)) {
            throw new AppException(ErrorCode.SPACE_NOT_FOUND);
        }
        List<Object[]> spaces = spaceRepo.findByNameWithChildren(id, requestBody.getLimitDepth());
        List<SpaceRaw> raws = spaces.stream().map(spaceMapper::toTreeRawFromObject).toList();
        return spaceMapper.toTreeFromListRaw(id, raws);
    }

}