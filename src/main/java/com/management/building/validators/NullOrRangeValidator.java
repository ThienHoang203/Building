package com.management.building.validators;

import java.math.BigDecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrRangeValidator {
    abstract static class BaseValidator<T extends Number> implements ConstraintValidator<NullOrRange, T> {

        protected boolean nullable;
        protected double min;
        protected double max;
        protected boolean minInclusive;
        protected boolean maxInclusive;

        @Override
        public void initialize(NullOrRange annotation) {
            this.nullable = annotation.nullable();
            this.min = annotation.min();
            this.max = annotation.max();
            this.minInclusive = annotation.minInclusive();
            this.maxInclusive = annotation.maxInclusive();
        }

        @Override
        public boolean isValid(T value, ConstraintValidatorContext context) {
            // Nếu null
            if (value == null) {
                return nullable;
            }
            double numValue = value.doubleValue();
            if (min != Double.NEGATIVE_INFINITY) {
                if (minInclusive && numValue < min) {
                    return false;
                }
                if (!minInclusive && numValue <= min) {
                    return false;
                }
            }
            if (max != Double.POSITIVE_INFINITY) {
                if (maxInclusive && numValue > max) {
                    return false;
                }
                if (!maxInclusive && numValue >= max) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class IntegerValidator extends BaseValidator<Integer> {
    }

    public static class LongValidator extends BaseValidator<Long> {
    }

    public static class DoubleValidator extends BaseValidator<Double> {
    }

    public static class FloatValidator extends BaseValidator<Float> {
    }

    public static class BigDecimalValidator extends BaseValidator<BigDecimal> {
        @Override
        public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
            if (value == null) {
                return nullable;
            }
            double numValue = value.doubleValue();
            if (min != Double.NEGATIVE_INFINITY) {
                if (minInclusive && numValue < min) {
                    return false;
                }
                if (!minInclusive && numValue <= min) {
                    return false;
                }
            }
            if (max != Double.POSITIVE_INFINITY) {
                if (maxInclusive && numValue > max) {
                    return false;
                }
                if (!maxInclusive && numValue >= max) {
                    return false;
                }
            }
            return true;
        }
    }
}
