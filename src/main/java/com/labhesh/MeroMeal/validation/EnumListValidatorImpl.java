package com.labhesh.MeroMeal.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class EnumListValidatorImpl implements ConstraintValidator<EnumListValidator, List<String>> {

    private List<String> acceptedValues;

    @Override
    public void initialize(EnumListValidator annotation) {
        System.out.println(annotation.enumClass().getEnumConstants());
        acceptedValues = List.of(annotation.enumClass().getEnumConstants()).stream()
                .map(Enum::name)
                .toList();
    }

    @Override
    public boolean isValid(List<String> values, ConstraintValidatorContext context) {
        if (values == null || values.isEmpty()) {
            return true;
        }
        System.out.println(acceptedValues);


        for (String value : values) {
            if (!acceptedValues.contains(value)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format("Invalid value '%s', accepted values are: %s", value, acceptedValues))
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
