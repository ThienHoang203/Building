package com.management.building.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankOrNullValidator.class)
public @interface NotBlankOrNull {
    boolean nullable() default true;

    String message() default "Field không được empty hoặc chỉ chứa khoảng trắng";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
