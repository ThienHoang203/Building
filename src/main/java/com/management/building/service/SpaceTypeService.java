package com.management.building.service;

import com.management.building.dto.request.SpaceTypeCreateRequest;
import com.management.building.dto.request.SpaceTypeUpdateRequest;
import com.management.building.dto.response.SpaceTypeResponse;
import com.management.building.entity.SpaceType;
import com.management.building.exception.AppException;
import com.management.building.exception.ErrorCode;
import com.management.building.mapper.SpaceTypeMapper;
import com.management.building.repository.SpaceTypeRepository;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpaceTypeService {

  SpaceTypeRepository spaceTypeRepo;
  SpaceTypeMapper spaceTypeMapper;

  public SpaceTypeResponse create(SpaceTypeCreateRequest request) {
    // if the primary key is name of this type exists, throw SPACE_TYPE_NAME_EXISTS
    // error
    if (spaceTypeRepo.existsById(request.getName())) {
      throw new AppException(ErrorCode.SPACE_TYPE_NAME_EXISTS);
    }

    // make the name uppercase
    request.setName(request.getName().toUpperCase());

    var spaceType = spaceTypeMapper.toSpaceTypeFromSpaceTypeCreateRequest(request);

    var saveResult = spaceTypeRepo.save(spaceType);

    var responseData = spaceTypeMapper.toSpaceTypeReponseFromSpaceType(saveResult);

    return responseData;
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
