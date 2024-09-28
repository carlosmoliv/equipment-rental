package com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs;

public record SignUpInput(
        String firstName,
        String lastName,
        String password,
        String email,
        String phoneNumber
) {
}
