package com.management.building.validators;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidEnumValidator implements ConstraintValidator<ValidEnum, Object> {

    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;
    private boolean allowNull;


    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.enumClass = constraintAnnotation.enumClass();
        this.ignoreCase = constraintAnnotation.ignoreCase();
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return allowNull;
        }

        if (enumClass == null || enumClass.getEnumConstants() == null) {
            return false;
        }

        // Handle Set<Enum> case
        if (value instanceof Set<?>) {
            Set<?> enumSet = (Set<?>) value;

            // Allow empty sets
            if (enumSet.isEmpty()) {
                return true;
            }

            // Validate each enum in the set
            for (Object item : enumSet) {
                if (item == null) {
                    if (!allowNull) {
                        return false;
                    }
                    continue;
                }

                // Check if item is an instance of the expected enum class
                if (!enumClass.isInstance(item)) {
                    return false;
                }
            }
            return true;
        }

        // Handle String case (for backward compatibility)
        if (value instanceof String) {
            String stringValue = (String) value;

            for (Object enumConstant : enumClass.getEnumConstants()) {
                String enumName = ((Enum<?>) enumConstant).name();

                if (ignoreCase) {
                    if (enumName.equalsIgnoreCase(stringValue)) {
                        return true;
                    }
                } else {
                    if (enumName.equals(stringValue)) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Handle single Enum case
        if (enumClass.isInstance(value)) {
            return true;
        }

        return false;
    }

}
