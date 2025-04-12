package com.carlosoliveira.equipment_rental.modules.rental.dtos;

import java.math.BigDecimal;

public record PaymentDetails(
        BigDecimal amount,
        String email,
        String creditCardToken
) {
}
