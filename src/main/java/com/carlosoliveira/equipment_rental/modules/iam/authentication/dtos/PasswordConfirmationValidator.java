package com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordMatches, SignUpDto> {

    @Override
    public boolean isValid(SignUpDto signUpDto, ConstraintValidatorContext context) {
        String password = signUpDto.password();
        String passwordConfirmation = signUpDto.passwordConfirmation();

        if (password == null || passwordConfirmation == null) {
            return false;
        }

        return signUpDto.password().equals(signUpDto.passwordConfirmation());
    }
}
