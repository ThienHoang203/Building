package com.management.building.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    EXCEPTION_UNCATEGORIZED(9999, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_EXISTS(8001, "Username already exists", HttpStatus.CONFLICT),
    SPACE_TYPE_NAME_EXISTS(8002, "This space type name already exists", HttpStatus.CONFLICT),
    ROLE_NAME_EXISTS(8003, "This role name already exists", HttpStatus.CONFLICT),
    SPACE_TYPE_NAME_INVALID(7000, "Space type name is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(7001, "Password is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(7002, "Email is invalid", HttpStatus.BAD_REQUEST),
    DATE_FORMAT_INVALID(7003, "Date must be yyyy-MM-dd", HttpStatus.BAD_REQUEST),
    DOB_INVALID(7011, "Date of birth is invalid, must be at least {min} years old", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED(7004, "{field} is required", HttpStatus.BAD_REQUEST),
    FIELD_SIZE_INVALID(7005, "{field} must be between {min} and {max} characters", HttpStatus.BAD_REQUEST),
    FIELD_MIN_INVALID(7006, "{field} must be at least {min}", HttpStatus.BAD_REQUEST),
    FIELD_MAX_INVALID(7007, "{field} must not exceed {max}", HttpStatus.BAD_REQUEST),
    FIELD_EMAIL_INVALID(7008, "{field} must be a valid email address", HttpStatus.BAD_REQUEST),
    FIELD_PATTERN_INVALID(7009, "{field} format is invalid", HttpStatus.BAD_REQUEST),
    ENUM_INVALID(7010, "{field} must be one of: {values}", HttpStatus.BAD_REQUEST),
    KEY_NOT_FOUND(6000, "Invalid message key", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(6001, "User not found", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(6002, "Permission not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(6003, "Role not found", HttpStatus.NOT_FOUND),
    AUTHENTICATION_FAILED(5000, "Username/Password is invalid", HttpStatus.UNAUTHORIZED),
    AUTHORIZATION_FAILED(5001, "Access forbidden", HttpStatus.FORBIDDEN),
    TOKEN_INVALID(5002, "Missing or invalid token", HttpStatus.UNAUTHORIZED),
    VALIDATION_ERROR(4000, "Validation failed", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
