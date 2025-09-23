package com.management.building.exception;

import com.management.building.dto.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  // private static final java.util.regex.Pattern PLACEHOLDER_PATTERN = java.util.regex.Pattern.compile("\\{([^}]+)\\}");

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ApiResponse<?>> handlingException(Exception exception) {

    log.info("cause: " + exception.getCause().getMessage());
    ErrorCode errorCode = ErrorCode.EXCEPTION_UNCATEGORIZED;
    return ResponseEntity.status(errorCode.getStatusCode()).body(
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
  }

  @ExceptionHandler(value = NoResourceFoundException.class)
  public ResponseEntity<ApiResponse<?>> handlingNoResourceFoundException(
      NoResourceFoundException exception) {
    log.info(exception.getMessage());
    ErrorCode errorCode = ErrorCode.PATH_VARIABLE_MISSING_OR_INVALID;
    return ResponseEntity.status(errorCode.getStatusCode()).body(
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
  }

  @ExceptionHandler(value = HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponse<?>> handlingDateFormatException(
      HttpMessageNotReadableException exception) {
    log.info("This is error");
    ErrorCode errorCode = ErrorCode.JSON_PARSE_ERROR;
    return ResponseEntity.status(errorCode.getStatusCode()).body(
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
  }

  @ExceptionHandler(value = AppException.class)
  public ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
    ErrorCode errorCode = exception.getErrorCode();
    return ResponseEntity.status(errorCode.getStatusCode()).body(ApiResponse.builder()
        .code(errorCode.getCode()).message(exception.getFormattedMessage()).build());
  }

  @ExceptionHandler(value = AuthorizationDeniedException.class)
  public ResponseEntity<ApiResponse<?>> handlingAccessDeniedException(
      AuthorizationDeniedException exception) {
    ErrorCode errorCode = ErrorCode.AUTHORIZATION_FAILED;
    return ResponseEntity.status(errorCode.getStatusCode()).body(
        ApiResponse.builder().code(errorCode.getCode()).message(errorCode.getMessage()).build());
  }

}
