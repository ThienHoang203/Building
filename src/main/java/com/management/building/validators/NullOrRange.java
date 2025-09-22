package com.management.building.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        NullOrRangeValidator.IntegerValidator.class,
        NullOrRangeValidator.LongValidator.class,
        NullOrRangeValidator.DoubleValidator.class,
        NullOrRangeValidator.FloatValidator.class,
        NullOrRangeValidator.BigDecimalValidator.class
})
public @interface NullOrRange {

    boolean nullable() default true;

    double min() default Double.NEGATIVE_INFINITY;

    double max() default Double.POSITIVE_INFINITY;

    boolean minInclusive() default true;

    boolean maxInclusive() default true;

    String message() default "Giá trị phải null hoặc nằm trong khoảng cho phép";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
