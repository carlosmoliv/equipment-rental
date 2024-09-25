package com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordMatches, SignUpDto> {

    @Override
    public boolean isValid(SignUpDto signUpDto, ConstraintValidatorContext context) {
        return signUpDto.password().equals(signUpDto.passwordConfirmation());
    }
}
