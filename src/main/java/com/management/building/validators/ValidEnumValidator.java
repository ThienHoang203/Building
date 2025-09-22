package com.management.building.validators;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, Object> {
    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;
    private boolean allowNull;
    private boolean allowEmptySet;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.enumClass = constraintAnnotation.enumClass();
        this.ignoreCase = constraintAnnotation.ignoreCase();
        this.allowNull = constraintAnnotation.allowNull();
        this.allowEmptySet = constraintAnnotation.allowEmptySet();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }
        if (enumClass == null || enumClass.getEnumConstants() == null) {
            return false;
        }
        if (value instanceof Set<?>) { // Handle Set<Enum> case
            Set<?> enumSet = (Set<?>) value;
            if (enumSet.isEmpty()) {
                return allowEmptySet;
            }
            for (Object item : enumSet) { // Validate each enum in the set
                if (item == null) {
                    if (!allowNull) {
                        return false;
                    }
                    continue;
                }
                if (!enumClass.isInstance(item)) {// Check if item is an instance of the expected enum class
                    return false;
                }
            }
            return true;
        } else if (value instanceof String) { // Handle String case (for backward compatibility)
            String stringValue = (String) value;
            for (Object enumConstant : enumClass.getEnumConstants()) {
                String enumName = ((Enum<?>) enumConstant).name();
                if (ignoreCase) {
                    if (enumName.equalsIgnoreCase(stringValue)) {
                        return true;
                    }
                } else if (enumName.equals(stringValue)) {
                    return true;
                }
            }
            return false;
        } else if (enumClass.isInstance(value)) {// Handle single Enum case
            return true;
        }
        return false;
    }
}
