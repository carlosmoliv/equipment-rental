package com.carlosoliveira.equipment_rental.modules.rental.presenters.dtos;

import jakarta.validation.constraints.NotBlank;

public record PayRentalDto(
        @NotBlank(message = "Credit Card token is required")
        String creditCardToken
) {
}
