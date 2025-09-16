package com.management.building.exception;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.management.building.dto.response.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.metadata.ConstraintDescriptor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final java.util.regex.Pattern PLACEHOLDER_PATTERN = java.util.regex.Pattern.compile("\\{([^}]+)\\}");

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<?>> handlingException(Exception exception) {
        ErrorCode errorCode = ErrorCode.EXCEPTION_UNCATEGORIZED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handlingDateFormatException(HttpMessageNotReadableException exception) {
        ErrorCode errorCode = ErrorCode.DATE_FORMAT_INVALID;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<?>> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(exception.getFormattedMessage())
                        .build());
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handlingAccessDeniedException(AuthorizationDeniedException exception) {
        ErrorCode errorCode = ErrorCode.AUTHORIZATION_FAILED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(
                ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    /**
     * Xử lý validation cho @RequestBody (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, ErrorDetail> errors = new HashMap<>();

        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();

        for (ObjectError error : objectErrors) {
            if (error instanceof FieldError fieldError) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                String finalMessage = "";
                // Check if message is an ErrorCode enum name
                ErrorCode errorCode = getErrorCodeFromMessage(errorMessage);

                if (errorCode != null) {
                    // Message is an ErrorCode enum name
                    finalMessage = buildErrorMessage(errorCode, fieldError, fieldName);
                    errors.put(fieldName, new ErrorDetail(errorCode.getCode(), finalMessage));
                } else {
                    // Use default message or map to appropriate ErrorCode
                    errorCode = mapFieldErrorToErrorCode(fieldError);
                    finalMessage = buildErrorMessage(errorCode, fieldError, fieldName);
                    errors.put(fieldName, new ErrorDetail(errorCode.getCode(), finalMessage));
                }

                log.debug("Field: {}, ErrorCode: {}, Message: {}",
                        fieldName, errorCode.name(), finalMessage);
            }
        }

        // Convert to simple map for response
        Map<String, String> errorMessages = errors.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().getMessage()));

        ErrorCode errorCodeResponse = ErrorCode.VALIDATION_ERROR;

        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCodeResponse.getCode())
                .message(errorCodeResponse.getMessage())
                .errors(errorMessages)
                .build();

        return ResponseEntity
                .status(errorCodeResponse.getStatusCode())
                .body(response);
    }

    /**
     * Xử lý validation cho @RequestParam và @PathVariable
     * (ConstraintViolationException)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(
            ConstraintViolationException ex) {

        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> getParameterName(violation),
                        violation -> {
                            String message = violation.getMessage();
                            ErrorCode errorCode = getErrorCodeFromMessage(message);

                            if (errorCode != null) {
                                // Message is an ErrorCode enum name
                                return buildErrorMessage(errorCode, violation);
                            } else {
                                // Map to appropriate ErrorCode
                                errorCode = mapConstraintViolationToErrorCode(
                                        violation);
                                return buildErrorMessage(errorCode, violation);
                            }
                        }));

        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.VALIDATION_ERROR.getCode())
                .message(ErrorCode.VALIDATION_ERROR.getMessage())
                .errors(errors)
                .build();

        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatusCode())
                .body(response);
    }

    /**
     * Try to get ErrorCode from message string
     */
    private ErrorCode getErrorCodeFromMessage(String message) {
        if (message == null) {
            return null;
        }

        try {
            // Try to parse message as ErrorCode enum name
            return ErrorCode.valueOf(message.trim());
        } catch (IllegalArgumentException e) {
            // Message is not an ErrorCode enum name
            return null;
        }
    }

    /**
     * Map FieldError to appropriate ErrorCode (fallback)
     */
    private ErrorCode mapFieldErrorToErrorCode(FieldError fieldError) {
        String validationCode = fieldError.getCode();
        String fieldName = fieldError.getField();

        // Check for specific field validations first
        if ("password".equalsIgnoreCase(fieldName)) {
            if ("Pattern".equals(validationCode)) {
                return ErrorCode.PASSWORD_INVALID;
            }
        }

        if ("email".equalsIgnoreCase(fieldName)) {
            if ("Email".equals(validationCode)) {
                return ErrorCode.EMAIL_INVALID;
            }
        }

        // Map general validation annotations
        switch (validationCode) {
            case "NotNull":
            case "NotBlank":
            case "NotEmpty":
                return ErrorCode.FIELD_REQUIRED;

            case "Size":
                return ErrorCode.FIELD_SIZE_INVALID;

            case "Min":
            case "DecimalMin":
            case "Positive":
            case "PositiveOrZero":
                return ErrorCode.FIELD_MIN_INVALID;

            case "Max":
            case "DecimalMax":
            case "Negative":
            case "NegativeOrZero":
                return ErrorCode.FIELD_MAX_INVALID;

            case "Email":
                return ErrorCode.FIELD_EMAIL_INVALID;

            case "Pattern":
                return ErrorCode.FIELD_PATTERN_INVALID;

            default:
                return ErrorCode.VALIDATION_ERROR;
        }
    }

    /**
     * Map ConstraintViolation to appropriate ErrorCode (fallback)
     */
    private ErrorCode mapConstraintViolationToErrorCode(ConstraintViolation<?> violation) {
        ConstraintDescriptor<?> descriptor = violation.getConstraintDescriptor();
        Annotation annotation = descriptor.getAnnotation();

        if (annotation instanceof NotNull
                || annotation instanceof NotBlank
                || annotation instanceof NotEmpty) {
            return ErrorCode.FIELD_REQUIRED;
        } else if (annotation instanceof Size) {
            return ErrorCode.FIELD_SIZE_INVALID;
        } else if (annotation instanceof Min || annotation instanceof DecimalMin) {
            return ErrorCode.FIELD_MIN_INVALID;
        } else if (annotation instanceof Max || annotation instanceof DecimalMax) {
            return ErrorCode.FIELD_MAX_INVALID;
        } else if (annotation instanceof Email) {
            return ErrorCode.FIELD_EMAIL_INVALID;
        } else if (annotation instanceof Pattern) {
            return ErrorCode.FIELD_PATTERN_INVALID;
        }

        return ErrorCode.VALIDATION_ERROR;
    }

    /**
     * Build error message from ErrorCode with placeholders for FieldError
     */
    private String buildErrorMessage(ErrorCode errorCode, FieldError fieldError, String fieldName) {
        String message = errorCode.getMessage();
        Map<String, Object> placeholders = new HashMap<>();

        // Add field name
        placeholders.put("field", fieldName);

        // Extract validation parameters
        extractValidationParameters(fieldError, placeholders);

        // Replace placeholders in message
        return replacePlaceholders(message, placeholders);
    }

    /**
     * Build error message from ErrorCode with placeholders for
     * ConstraintViolation
     */
    private String buildErrorMessage(ErrorCode errorCode, ConstraintViolation<?> violation) {
        String message = errorCode.getMessage();
        Map<String, Object> placeholders = new HashMap<>();

        // Add field name
        String fieldName = getParameterName(violation);
        placeholders.put("field", fieldName);

        // Extract validation parameters
        extractValidationParameters(violation, placeholders);

        // Replace placeholders in message
        return replacePlaceholders(message, placeholders);
    }

    /**
     * Extract validation parameters from FieldError
     */
    private void extractValidationParameters(FieldError fieldError, Map<String, Object> placeholders) {
        Object[] args = fieldError.getArguments();
        if (args == null || args.length == 0) {
            return;
        }

        String code = fieldError.getCode();

        switch (code) {
            case "Size":
                if (args.length >= 3) {
                    placeholders.put("min", args[2]);
                    placeholders.put("max", args[1]);
                }
                break;

            case "Min":
            case "DecimalMin":
                if (args.length >= 2) {
                    placeholders.put("min", args[1]);
                }
                break;

            case "Max":
            case "DecimalMax":
                if (args.length >= 2) {
                    placeholders.put("max", args[1]);
                }
                break;

            case "Pattern":
                if (args.length >= 2) {
                    placeholders.put("pattern", args[1]);
                }
                break;

            case "DobConstraint":
                if (args.length >= 2) {
                    placeholders.put("min", args[1]);
                }
                break;
            case "ValidEnum":
                if (args.length >= 1) {
                    placeholders.put("min", args[1]);
                }
                break;
        }
    }

    /**
     * Extract validation parameters from ConstraintViolation
     */
    private void extractValidationParameters(ConstraintViolation<?> violation,
            Map<String, Object> placeholders) {
        ConstraintDescriptor<?> descriptor = violation.getConstraintDescriptor();
        Map<String, Object> attributes = descriptor.getAttributes();
        Annotation annotation = descriptor.getAnnotation();

        if (annotation instanceof Size) {
            placeholders.put("min", attributes.get("min"));
            placeholders.put("max", attributes.get("max"));
        } else if (annotation instanceof Min) {
            placeholders.put("min", attributes.get("value"));
        } else if (annotation instanceof Max) {
            placeholders.put("max", attributes.get("value"));
        } else if (annotation instanceof DecimalMin) {
            placeholders.put("min", attributes.get("value"));
        } else if (annotation instanceof DecimalMax) {
            placeholders.put("max", attributes.get("value"));
        } else if (annotation instanceof Pattern) {
            placeholders.put("pattern", attributes.get("regexp"));
        } else if (annotation instanceof Digits) {
            placeholders.put("integer", attributes.get("integer"));
            placeholders.put("fraction", attributes.get("fraction"));
        }

        // Add invalid value if present
        Object invalidValue = violation.getInvalidValue();
        if (invalidValue != null) {
            placeholders.put("value", invalidValue.toString());
        }
    }

    /**
     * Replace placeholders in message
     */
    private String replacePlaceholders(String message, Map<String, Object> placeholders) {
        if (message == null) {
            return "";
        }

        java.util.regex.Matcher matcher = PLACEHOLDER_PATTERN.matcher(message);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            Object value = placeholders.get(placeholder);

            if (value != null) {
                matcher.appendReplacement(result, java.util.regex.Matcher.quoteReplacement(value.toString()));
            } else {
                // Keep placeholder if no value found
                matcher.appendReplacement(result, java.util.regex.Matcher.quoteReplacement(matcher.group(0)));
            }
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Get parameter name from ConstraintViolation
     */
    private String getParameterName(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        String[] parts = path.split("\\.");
        return parts[parts.length - 1];
    }

    /**
     * Handle missing request parameter
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex) {

        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("field", ex.getParameterName());

        String message = replacePlaceholders(
                ErrorCode.FIELD_REQUIRED.getMessage(),
                placeholders);

        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.FIELD_REQUIRED.getCode())
                .message(message)
                .build();

        return ResponseEntity
                .status(ErrorCode.FIELD_REQUIRED.getStatusCode())
                .body(response);
    }

    /**
     * Inner class to hold error details
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    private static class ErrorDetail {

        private int code;
        private String message;
    }
}
