package com.management.building.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotBlankOrNullValidator implements ConstraintValidator<NotBlankOrNull, String> {

    private boolean nullable;

    @Override
    public void initialize(NotBlankOrNull annotation) {
        this.nullable = annotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return nullable;
        }
        return !value.trim().isEmpty();
    }
}