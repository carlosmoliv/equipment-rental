package com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos;

public record SignUpDto(
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String password,
        String passwordConfirmation
) {
}