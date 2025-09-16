package com.management.building.exception;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
    private Map<String, Object> attributes;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, Map<String, Object> attributes) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.attributes = attributes;
    }

    public String getFormattedMessage() {
        String message = errorCode.getMessage();
        if (attributes != null && !attributes.isEmpty()) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                message = message.replace(placeholder, String.valueOf(entry.getValue()));
            }
        }
        return message;
    }
}
