package com.smoothstack.userservice.util;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidPassword.PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE }) //specifies the contexts in which the ValidPassword annotation can be applied.
@Retention(RetentionPolicy.RUNTIME) //annotation is available at runtime.
public @interface ValidPassword {
    String message() default "Invalid password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

        private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9 ]).{8,100}$";

        @Override
        public boolean isValid(String password, ConstraintValidatorContext context) {
            if (password == null) {
                return true;
            }
            return password.matches(PASSWORD_PATTERN);
        }
    }
}