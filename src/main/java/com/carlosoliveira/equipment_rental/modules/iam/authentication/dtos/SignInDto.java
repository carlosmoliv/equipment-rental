package com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignInDto(
        @Email
        String email,

        @NotNull
        @Size(min = 6)
        String password
) {
}
