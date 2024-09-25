package com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos;

import jakarta.validation.constraints.Email;

public record SignInDto(
        @Email
        String email,

        String password
) {
}
