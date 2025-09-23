package com.management.building.service.space;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeTreeRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.space.SpaceTypeRaw;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeTreeResponse;
import com.management.building.entity.space.SpaceType;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.space.SpaceTypeMapper;
import com.management.building.repository.space.SpaceTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SpaceTypeServiceImplement implements SpaceTypeService {
  SpaceTypeRepository spaceTypeRepo;
  SpaceTypeMapper spaceTypeMapper;

  @Override
  public SpaceTypeResponse create(SpaceTypeCreateRequest requestBody) {
    // validate name
    String name = requestBody.getName().trim().toUpperCase();
    if (spaceTypeRepo.existsById(name)) {
      throw new AppException(ErrorCode.SPACE_TYPE_NAME_EXISTS);
    }
    requestBody.setName(name);
    // validate parent
    int level = requestBody.getLevel();
    int parentLevel = level - 1;
    String parentName = requestBody.getParentName();
    SpaceType parent = null;
    if (StringUtils.hasText(parentName)) {
      parentName = parentName.trim().toUpperCase();
      if (parentName.equals(name)) {
        throw new AppException(ErrorCode.PARENT_HAS_SAME_CHILD_ID);
      }
      parent = spaceTypeRepo.findByNameAndLevel(parentName, parentLevel)
          .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_TYPE_IN_LEVEL_NOT_FOUND));
    }
    var entity = spaceTypeMapper.toSpaceTypeFromCreateRequest(requestBody);
    entity.setParent(parent);
    entity.setLevel(level);
    try {
      var data = spaceTypeRepo.save(entity);
      return spaceTypeMapper.toReponseFromSpaceType(data);
    } catch (DataIntegrityViolationException e) {
      throw new AppException(ErrorCode.UNSUCCESS);
    } catch (OptimisticLockingFailureException e) {
      throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
    } catch (Exception e) {
      throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
    }
  }

  @Override
  @Transactional
  public List<SpaceTypeResponse> getAll() {
    var data = spaceTypeRepo.findAll();
    return data.stream().map(spaceTypeMapper::toReponseFromSpaceType).toList();
  }

  @Override
  public SpaceTypeResponse getByName(String name) {
    name = name.trim().toUpperCase();
    var data = spaceTypeRepo.findById(name).orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
    return spaceTypeMapper.toReponseFromSpaceType(data);
  }

  @Override
  public SpaceTypeResponse update(String name, SpaceTypeUpdateRequest requestBody) {
    var entity = spaceTypeRepo.findById(name.trim().toUpperCase())
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
    spaceTypeMapper.updateSpaceTypeFromUpdateRequest(requestBody, entity);
    var result = spaceTypeRepo.save(entity);
    return spaceTypeMapper.toReponseFromSpaceType(result);
  }

  @Override
  public SpaceTypeResponse updateParent(String name, String parentName) {
    name = name.trim().toUpperCase();
    parentName = parentName.trim().toUpperCase();
    if (name.equals(parentName)) {
      throw new AppException(ErrorCode.PARENT_HAS_SAME_CHILD_ID);
    }
    var entity = spaceTypeRepo.findByNameWithParent(name)
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
    int parentLevel = entity.getLevel() - 1;
    var newParent = spaceTypeRepo.findByNameAndLevel(parentName, parentLevel)
        .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_TYPE_IN_LEVEL_NOT_FOUND));
    entity.setParent(newParent);
    var result = spaceTypeRepo.save(entity);
    return spaceTypeMapper.toReponseFromSpaceType(result);
  }

  @Override
  @Transactional
  public void delete(String name) {
    name = name.trim().toUpperCase();
    if (!spaceTypeRepo.existsById(name)) {
      throw new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND);
    }
    try {
      spaceTypeRepo.deleteById(name);
    } catch (OptimisticLockingFailureException e) {
      throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
    } catch (Exception e) {
      log.error("Unexpected error while deleting SpaceType: {}", name, e);
      throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
    }
  }

  @Override
  @Transactional
  public SpaceTypeTreeResponse getSpaceTypeTree(String name, SpaceTypeTreeRequest request) {
    name = name.trim().toUpperCase();
    if (!spaceTypeRepo.existsById(name)) {
      throw new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND);
    }
    List<Object[]> spaceType = spaceTypeRepo.findByNameWithChildren(name, request.getLimitDepth());
    List<SpaceTypeRaw> raws = spaceType.stream().map(spaceTypeMapper::toTreeRawFromObject).toList();
    return spaceTypeMapper.toTreeFromListRaw(name, raws);
  }
}
