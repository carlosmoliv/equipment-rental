package com.carlosoliveira.carrental.modules.auth.authentication.dtos;

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
