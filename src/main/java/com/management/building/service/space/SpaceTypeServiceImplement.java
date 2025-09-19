package com.management.building.service.space;

import com.management.building.dto.request.space.SpaceTypeCreateRequest;
import com.management.building.dto.request.space.SpaceTypeUpdateRequest;
import com.management.building.dto.response.space.SpaceTypeResponse;
import com.management.building.entity.space.SpaceType;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.space.SpaceTypeMapper;
import com.management.building.repository.space.SpaceTypeRepository;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceTypeServiceImplement implements SpaceTypeService {

  SpaceTypeRepository spaceTypeRepo;
  SpaceTypeMapper spaceTypeMapper;

  public SpaceTypeResponse create(SpaceTypeCreateRequest requestBody) {
    checkCreateContraints(requestBody); // This function will throw an exception if the request body is not validated
    requestBody.setName(requestBody.getName().toUpperCase());

    Integer parentLevel = getParentLevel(requestBody.getParentSpaceTypeId());
    SpaceType spaceType = spaceTypeMapper.toSpaceTypeFromSpaceTypeCreateRequest(requestBody);
    spaceType.setLevel(parentLevel + 1);
    if (requestBody.getParentSpaceTypeId() != null) {
      if (!spaceTypeRepo.existsById(requestBody.getParentSpaceTypeId()))
        throw new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND);
      spaceType.setParentSpaceTypeId(requestBody.getParentSpaceTypeId());
    }

    var saveResult = spaceTypeRepo.save(spaceType);
    // after saving, update child-space's level
    if (CollectionUtils.isEmpty(requestBody.getChildSpaceTypeNames())) {
      spaceTypeRepo.updateSubtreeLevels(saveResult.getName());
    }
    var responseData = spaceTypeMapper.toSpaceTypeReponseFromSpaceType(saveResult);

    return responseData;
  }

  private Integer getParentLevel(String parentName) {
    var result = spaceTypeRepo.getParentLevel(parentName).orElse(null);
    return result == null ? 0 : (Integer) result[0];
  }

  private void checkCreateContraints(SpaceTypeCreateRequest requestBody) {
    if (spaceTypeRepo.existsById(requestBody.getName()))
      throw new AppException(ErrorCode.SPACE_TYPE_NAME_EXISTS);
    if (requestBody.getParentSpaceTypeId() != null) {
      if (requestBody.getName() == requestBody.getParentSpaceTypeId())
        throw new AppException(ErrorCode.PARENT_HAS_SAME_CHILD_ID);
      else if (spaceTypeRepo.existsById(requestBody.getParentSpaceTypeId()))
        throw new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND);
    }
    if (!CollectionUtils.isEmpty(requestBody.getFunctions()) && requestBody.getFunctions().contains(null))
      throw new AppException(ErrorCode.COLLECTION_CONTAIN_NULL);
    if (!CollectionUtils.isEmpty(requestBody.getChildSpaceTypeNames())
        && requestBody.getChildSpaceTypeNames().contains(null))
      throw new AppException(ErrorCode.COLLECTION_CONTAIN_NULL);
  }

  public List<SpaceTypeResponse> getSpaceTypes() {
    var result = spaceTypeRepo.findAll();

    var reponseData = result.stream().map(spaceTypeMapper::toSpaceTypeReponseFromSpaceType).toList();

    return reponseData;
  }

  public SpaceTypeResponse getSpaceTypeByName(String name) {
    SpaceType result = spaceTypeRepo.findById(name).orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));
    return spaceTypeMapper.toSpaceTypeReponseFromSpaceType(result);
  }

  public SpaceTypeResponse update(String name, SpaceTypeUpdateRequest requestBody) {
    var originalObject = spaceTypeRepo.findById(name)
        .orElseThrow(() -> new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND));

    spaceTypeMapper.updateSpaceTypeFromSpaceTypeUpdateRequest(requestBody, originalObject);

    var result = spaceTypeRepo.save(originalObject);

    return spaceTypeMapper.toSpaceTypeReponseFromSpaceType(result);
  }

  public void delete(String name) {
    if (!existsByName(name)) {
      throw new AppException(ErrorCode.SPACE_TYPE_NOT_FOUND);
    }

    try {
      spaceTypeRepo.deleteById(name);
    } catch (OptimisticLockingFailureException e) {
      throw new AppException(ErrorCode.DELETE_FAILED);
    } catch (DataIntegrityViolationException e) {
      Throwable throwable = e.getCause();
      if (throwable instanceof org.hibernate.exception.ConstraintViolationException cve) {
        var message = cve.getCause().getMessage();
        if (message != null) {
          if (message.contains("the REFERENCE constraint")) {
            throw new AppException(ErrorCode.FOREIGN_KEY_VIOLATION);
          }
        }
      }
      throw new RuntimeException("Lỗi tính toàn vẹn dữ liệu", e);
    }

    return;
  }

  public boolean existsByName(String name) {
    return spaceTypeRepo.existsById(name);
  }
}
