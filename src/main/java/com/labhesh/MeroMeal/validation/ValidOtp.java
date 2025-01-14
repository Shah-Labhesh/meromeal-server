package com.labhesh.MeroMeal.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = OtpValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOtp {
    String message() default "Invalid OTP. Must be a 5-digit numeric code.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
