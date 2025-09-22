package com.management.building.service.space;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateWithSpacesRequest;
import com.management.building.dto.response.space.SpaceTypeHierarchyResponse;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.dto.response.space.SpaceTypeWithSpacesResponse;
import com.management.building.entity.space.SpaceType;
import com.management.building.enums.space.LoadingHierarchyMode;
import com.management.building.enums.space.UpdateListSpacesMode;
import com.management.building.enums.space.UpdateParentMode;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.space.SpaceTypeMapper;
import com.management.building.repository.space.SpaceRepository;
import com.management.building.repository.space.SpaceTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
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
  SpaceRepository spaceRepo;
  SpaceTypeMapper spaceTypeMapper;

  @Override
  public SpaceTypeResponse create(SpaceTypeCreateRequest requestBody) {
    // validate name
    String name = requestBody.getName();
    if (isValidString(name)) {
      name = name.trim().toUpperCase();
      if (spaceTypeRepo.existsById(name)) {
        throw new AppException(ErrorCode.SPACE_TYPE_NAME_EXISTS);
      }
      requestBody.setName(name);
    }
    // validate parent
    var parentName = requestBody.getParentName();
    SpaceType parent = null;
    if (isValidString(parentName)) {
      parentName = parentName.trim().toUpperCase();
      parent = spaceTypeRepo.findById(parentName)
          .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_TYPE_NOT_FOUND));
    }
    var entity = spaceTypeMapper.toSpaceTypeFromCreateRequest(requestBody);
    entity.setParent(parent);
    entity.setLevel(parent == null ? 0 : parent.getLevel() + 1);
    try {
      var data = spaceTypeRepo.save(entity);
      return spaceTypeMapper.toReponseFromSpaceType(data);
    } catch (DataIntegrityViolationException e) {
      throw new AppException(ErrorCode.SPACE_TYPE_NAME_EXISTS);
    } catch (OptimisticLockingFailureException e) {
      throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
    } catch (Exception e) {
      throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
    }

  }

  @Override
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
  public List<SpaceTypeHierarchyResponse> getParents(String name, LoadingHierarchyMode mode) {
    name = name.trim().toUpperCase();

    return switch (mode) {
      case IMMEDIATE -> getByNameWithDirectParent(name);
      case FULL_PATH -> getByNameWithParents(name);
    };
  }

  @Override
  public List<SpaceTypeHierarchyResponse> getChildren(String name) {
    var data = spaceTypeRepo.findChildrenByCurrentName(name.trim().toUpperCase());
    return data.stream().map(spaceTypeMapper::toHierarchyResponseFromObject).toList();
  }

  @Override
  public SpaceTypeWithSpacesResponse getByNameWithSpaces(String name) {
    var data = spaceTypeRepo.findByNameWithSpaces(name)
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
    var repsonse = spaceTypeMapper.toResponseWithSpacesFromSpaceType(data);
    return repsonse;
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
  @Transactional
  public SpaceTypeWithSpacesResponse updateWithSpaces(String name, UpdateListSpacesMode mode,
      SpaceTypeUpdateWithSpacesRequest requestBody) {
    name = name.trim().toUpperCase();
    var spaceType = spaceTypeRepo.findByNameWithSpaces(name)
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));

    switch (mode) {
      case ADD:
        // Add new spaces to this space type (keeping existing ones)
        if (requestBody.getIds() != null && !requestBody.getIds().isEmpty()) {
          var spaces = spaceRepo.findAllById(requestBody.getIds());
          if (spaces.size() != requestBody.getIds().size()) {
            throw new AppException(ErrorCode.SPACE_NOT_FOUND);
          }

          for (var space : spaces) {
            space.setType(spaceType);
          }
          try {
            spaceRepo.saveAll(spaces);
          } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SPACE_TYPE_UPDATE_FAILED);
          } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
          } catch (Exception e) {
            log.error("Unexpected error while adding spaces to SpaceType: {}", name, e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
          }
        }
        break;

      case REPLACE:
        // Replace all existing spaces with new ones
        // First remove all current spaces
        if (spaceType.getSpaces() != null && !spaceType.getSpaces().isEmpty()) {
          var currentSpaces = new ArrayList<>(spaceType.getSpaces());
          for (var space : currentSpaces) {
            space.setType(null);
          }
          spaceRepo.saveAll(currentSpaces);
        }

        // Then add new spaces
        if (requestBody.getIds() != null && !requestBody.getIds().isEmpty()) {
          var newSpaces = spaceRepo.findAllById(requestBody.getIds());
          if (newSpaces.size() != requestBody.getIds().size()) {
            throw new AppException(ErrorCode.SPACE_NOT_FOUND);
          }
          for (var space : newSpaces) {
            space.setType(spaceType);
          }
          try {
            spaceRepo.saveAll(newSpaces);
          } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.SPACE_TYPE_UPDATE_FAILED);
          } catch (OptimisticLockingFailureException e) {
            throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
          } catch (Exception e) {
            log.error("Unexpected error while replacing spaces for SpaceType: {}", name, e);
            throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
          }
        }
        break;
    }

    var updatedSpaceType = spaceTypeRepo.findByNameWithSpaces(name)
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
    var response = spaceTypeMapper.toResponseWithSpacesFromSpaceType(updatedSpaceType);
    return response;
  }

  @Override
  @Transactional
  public SpaceTypeResponse updateParent(String name, String parentName, UpdateParentMode mode) {
    name = name.trim().toUpperCase();
    parentName = parentName.trim().toUpperCase();
    if (name.equals(parentName)) {
      throw new AppException(ErrorCode.PARENT_HAS_SAME_CHILD_ID);
    }
    var entity = spaceTypeRepo.findByNameWithParent(name)
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));

    switch (mode) {
      case NULL:
        if (entity.getParent() != null) {
          if (!entity.getParent().getName().equals(parentName)) {
            throw new AppException(ErrorCode.SPACE_TYPE_HAS_DIFFERENT_PARENT);
          }
        } else {
          throw new AppException(ErrorCode.SPACE_TYPE_HAS_NO_PARENT);
        }
        entity.setParent(null);
        entity.setLevel(0);
        return spaceTypeMapper.toReponseFromSpaceType(entity);
      case NEW:
        if (entity.getParent() != null && entity.getParent().getName().equals(parentName)) {
          throw new AppException(ErrorCode.SAME_OLD_PARENT);
        }
        var newParent = spaceTypeRepo.findById(parentName)
            .orElseThrow(() -> new AppException(ErrorCode.PARENT_SPACE_TYPE_NOT_FOUND));
        if (newParent.getLevel() > entity.getLevel()) {
          throw new AppException(ErrorCode.INVALID_PARENT_LEVEL);
        }
        entity.setParent(newParent);
        entity.setLevel(newParent.getLevel() + 1);
        break;
    }
    entity = spaceTypeRepo.save(entity);
    spaceTypeRepo.updateChildSpaceLevels(entity.getName());
    return spaceTypeMapper.toReponseFromSpaceType(entity);
  }

  @Override
  public void delete(String name) {
    name = name.trim().toUpperCase();
    if (!spaceTypeRepo.existsById(name)) {
      throw new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND);
    }
    if (spaceTypeRepo.countChildrenByParentName(name) > 0) {
      throw new AppException(ErrorCode.HAS_CHILDREN);
    }
    try {
      spaceTypeRepo.deleteById(name);
    } catch (DataIntegrityViolationException e) {
      throw new AppException(ErrorCode.SPACE_TYPE_IN_USE);
    } catch (OptimisticLockingFailureException e) {
      throw new AppException(ErrorCode.OPTIMISTIC_LOCKING_FAILURE);
    } catch (Exception e) {
      log.error("Unexpected error while deleting SpaceType: {}", name, e);
      throw new AppException(ErrorCode.EXCEPTION_UNCATEGORIZED);
    }
  }

  private boolean isValidString(String str) {
    return str != null && StringUtils.hasText(str);
  }

  private List<SpaceTypeHierarchyResponse> getByNameWithDirectParent(String name) {
    var data = spaceTypeRepo.findByNameWithParent(name)
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
    var parent = data.getParent();
    List<SpaceTypeHierarchyResponse> response = new ArrayList<>(2);
    response.add(spaceTypeMapper.toReponseWithParentFromSpaceType(data));
    if (parent != null) {
      response.add(spaceTypeMapper.toReponseWithParentFromSpaceType(parent));
    }
    return response;
  }

  private List<SpaceTypeHierarchyResponse> getByNameWithParents(String name) {
    var data = spaceTypeRepo.findRootsByCurrentName(name);
    return data.stream().map(spaceTypeMapper::toHierarchyResponseFromObject).toList();
  }

}
