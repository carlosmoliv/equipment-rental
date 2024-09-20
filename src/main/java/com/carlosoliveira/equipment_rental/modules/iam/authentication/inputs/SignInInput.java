package com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs;

public record SignInInput(
        String password,
        String email
) {
}
