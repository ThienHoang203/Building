package com.management.building.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
        EXCEPTION_UNCATEGORIZED(
                        9999,
                        "Unknown error",
                        HttpStatus.INTERNAL_SERVER_ERROR),
        FOREIGN_KEY_VIOLATION(
                        1090,
                        "Cannot delete because this record is being used by other data",
                        HttpStatus.BAD_REQUEST),
        CHECK_CONSTRAINT_VIOLATION(
                        1005,
                        "Check constraint violation",
                        HttpStatus.BAD_REQUEST),
        DELETE_FAILED(
                        9990,
                        "Server error, please delete later",
                        HttpStatus.INTERNAL_SERVER_ERROR),
        USERNAME_EXISTS(
                        8001,
                        "Username already exists",
                        HttpStatus.CONFLICT),
        SPACE_TYPE_NAME_EXISTS(
                        8002,
                        "This space type name already exists",
                        HttpStatus.CONFLICT),
        ROLE_NAME_EXISTS(
                        8003,
                        "This role name already exists",
                        HttpStatus.CONFLICT),
        SUPPLER_CODE_EXISTS(
                        8032,
                        "This supplier's code already exists",
                        HttpStatus.CONFLICT),
        SMART_DEVICE_CATEGORY_NAME_EXISTS(
                        8132,
                        "This smart device cateogory's name already exists",
                        HttpStatus.CONFLICT),
        SMART_DEVICE_CODE_EXISTS(
                        8123,
                        "This smart device code already exists",
                        HttpStatus.CONFLICT),
        SMART_DEVICE_NAME_EXISTS(
                        8124,
                        "This smart device name already exists",
                        HttpStatus.CONFLICT),
        SPACE_TYPE_NAME_INVALID(
                        7000,
                        "Space type name is invalid",
                        HttpStatus.BAD_REQUEST),
        PASSWORD_INVALID(
                        7001,
                        "Password is invalid",
                        HttpStatus.BAD_REQUEST),
        EMAIL_INVALID(
                        7002,
                        "Email is invalid",
                        HttpStatus.BAD_REQUEST),
        DATE_FORMAT_INVALID(
                        7003,
                        "Date must be yyyy-MM-dd",
                        HttpStatus.BAD_REQUEST),
        DOB_INVALID(
                        7011,
                        "Date of birth is invalid, must be at least {min} years old",
                        HttpStatus.BAD_REQUEST),
        SUPPLIER_NAME_LENGTH_INVALID(
                        7311,
                        "Suppplier's name length is invalid, must be in range 100 and 2",
                        HttpStatus.BAD_REQUEST),
        SUPPLIER_CODE_LENGTH_INVALID(
                        7311,
                        "Suppplier's code length is invalid, must be in range 50 and 3",
                        HttpStatus.BAD_REQUEST),
        FIELD_REQUIRED(
                        7004,
                        "{field} is required",
                        HttpStatus.BAD_REQUEST),
        FIELD_SIZE_INVALID(
                        7005,
                        "{field} must be between {min} and {max} characters",
                        HttpStatus.BAD_REQUEST),
        FIELD_MIN_INVALID(
                        7006,
                        "{field} must be at least {min}",
                        HttpStatus.BAD_REQUEST),
        FIELD_MAX_INVALID(
                        7007,
                        "{field} must not exceed {max}",
                        HttpStatus.BAD_REQUEST),
        FIELD_EMAIL_INVALID(
                        7008,
                        "{field} must be a valid email address",
                        HttpStatus.BAD_REQUEST),
        FIELD_PATTERN_INVALID(
                        7009, "{field} format is invalid",
                        HttpStatus.BAD_REQUEST),
        ENUM_INVALID(
                        7010,
                        "{field} must be one of: {values}",
                        HttpStatus.BAD_REQUEST),
        INVALID_PARENT_LEVEL(
                        7010,
                        "The parent level can not be less than the current level",
                        HttpStatus.BAD_REQUEST),
        INVALID_PARENT_SELF(
                        7010,
                        "The parent can not be the same as itself",
                        HttpStatus.BAD_REQUEST),
        COLLECTION_CONTAIN_NULL(
                        7011,
                        "The collection must not contain any null value",
                        HttpStatus.BAD_REQUEST),
        PARENT_HAS_SAME_CHILD_ID(
                        7011, "Parent can not have same id with child",
                        HttpStatus.CONFLICT),
        PATH_VARIABLE_MISSING_OR_INVALID(
                        7012,
                        "the url is missing path or invalid path",
                        HttpStatus.BAD_REQUEST),
        SAME_OLD_PARENT(
                        7230,
                        "The new parent is the same as the old parent",
                        HttpStatus.BAD_REQUEST),
        SPACE_TYPE_HAS_NO_PARENT(
                        7232,
                        "This space type has no parent",
                        HttpStatus.BAD_REQUEST),
        SPACE_TYPE_HAS_DIFFERENT_PARENT(
                        7014,
                        "This space type has a different parent",
                        HttpStatus.CONFLICT),
        PARENT_IS_IN_CHILD_ID(
                        7011, "Parent can not be in child list",
                        HttpStatus.CONFLICT),
        HAS_CHILDREN(
                        7011, "Cannot delete because it has children",
                        HttpStatus.CONFLICT),
        SPACE_TYPE_IN_USE(
                        7011, "Cannot delete space type because it is being used by existing spaces",
                        HttpStatus.CONFLICT),
        JSON_PARSE_ERROR(
                        70012,
                        "Invalid JSON format",
                        HttpStatus.BAD_REQUEST),
        KEY_NOT_FOUND(
                        6000, "Invalid message key",
                        HttpStatus.INTERNAL_SERVER_ERROR),
        USER_NOT_FOUND(
                        6001, "User not found",
                        HttpStatus.NOT_FOUND),
        PERMISSION_NOT_FOUND(
                        6002,
                        "Permission not found",
                        HttpStatus.NOT_FOUND),
        ROLE_NOT_FOUND(
                        6003,
                        "Role not found",
                        HttpStatus.NOT_FOUND),
        SPACE_TYPE_NOT_FOUND(
                        6004,
                        "Space type not found",
                        HttpStatus.NOT_FOUND),
        SPACE_NOT_FOUND(
                        6005,
                        "Space type not found",
                        HttpStatus.NOT_FOUND),
        PARENT_SPACE_NOT_FOUND(
                        6006, "The parent space not found",
                        HttpStatus.NOT_FOUND),
        PARENT_SPACE_TYPE_IN_LEVEL_NOT_FOUND(
                        6006, "There is not parent with this name and level",
                        HttpStatus.NOT_FOUND),
        PARENT_SPACE_IN_LEVEL_NOT_FOUND(
                        6993, "There is not parent with this name and level",
                        HttpStatus.NOT_FOUND),
        PARENT_SPACE_TYPE_NOT_FOUND(
                        6007, "The parent space type not found",
                        HttpStatus.NOT_FOUND),
        SUPPLIER_NOT_FOUND(
                        6017, "The supplier not found",
                        HttpStatus.NOT_FOUND),
        SMART_DEVICE_CATEGORY_NOT_FOUND(
                        6212, "The smart device category not found",
                        HttpStatus.NOT_FOUND),
        SMART_DEVICE_NOT_FOUND(
                        6213, "The smart device  not found",
                        HttpStatus.NOT_FOUND),
        LIST_NOT_CONTAIN_ALL(
                        6100, "The list does not contain all the provided elements",
                        HttpStatus.NOT_FOUND),
        AUTHENTICATION_FAILED(
                        5000,
                        "Username/Password is invalid",
                        HttpStatus.UNAUTHORIZED),
        AUTHORIZATION_FAILED(
                        5001,
                        "Access forbidden",
                        HttpStatus.FORBIDDEN),
        TOKEN_INVALID(
                        5002,
                        "Missing or invalid token",
                        HttpStatus.UNAUTHORIZED),
        VALIDATION_ERROR(
                        4000,
                        "Validation failed",
                        HttpStatus.BAD_REQUEST),
        SPACE_TYPE_UPDATE_FAILED(
                        4000,
                        "Failed to update space type",
                        HttpStatus.BAD_REQUEST),
        UNSUCCESS(
                        3234,
                        "action failed",
                        HttpStatus.BAD_REQUEST),
        OPTIMISTIC_LOCKING_FAILURE(
                        9001,
                        "The record you are trying to update has been modified by another transaction. Please reload and try again.",
                        HttpStatus.CONFLICT),
        SPACE_CREATE_FAILED(500, "Failed to create space", HttpStatus.INTERNAL_SERVER_ERROR),
        SPACE_UPDATE_FAILED(500, "Failed to update space", HttpStatus.INTERNAL_SERVER_ERROR),
        SPACE_IN_USE(409, "Space cannot be deleted because it is in use", HttpStatus.BAD_REQUEST),
        SPACE_HAS_DIFFERENT_PARENT(409, "Space has a different parent than specified",
                        HttpStatus.BAD_REQUEST),
        SPACE_HAS_NO_PARENT(404, "Space has no parent to remove", HttpStatus.BAD_REQUEST),
        CIRCULAR_DEPENDENCY(409, "Cannot set parent due to circular dependency", HttpStatus.BAD_REQUEST),
        INVALID_INPUT(400, "Invalid input provided", HttpStatus.BAD_REQUEST);

        int code;
        String message;
        HttpStatus statusCode;

        ErrorCode(int code, String message, HttpStatus statusCode) {
                this.code = code;
                this.message = message;
                this.statusCode = statusCode;
        }
}
