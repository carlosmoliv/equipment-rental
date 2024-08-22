package com.carlosoliveira.carrental.modules.auth.authentication.inputs;

public record SignUpInput(
        String username,
        String firstName,
        String lastName,
        String password,
        String email,
        String phoneNumber
) {
}
