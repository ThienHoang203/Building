package com.management.building.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEnumValidator.class)
public @interface ValidEnum {
    String message() default "ENUM_INVALID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Required: enum class cần validate
    Class<? extends Enum<?>> enumClass();

    // Optional: các options bổ sung
    boolean ignoreCase() default false;

    boolean allowNull() default false;

    boolean allowEmptySet() default false;
}