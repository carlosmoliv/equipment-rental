package com.carlosoliveira.equipment_rental.modules.rental.application.exceptions;

public class PaymentStatusException extends RuntimeException {
    public PaymentStatusException() {
        super("Only PENDING rental can be paid.");
    }
}
