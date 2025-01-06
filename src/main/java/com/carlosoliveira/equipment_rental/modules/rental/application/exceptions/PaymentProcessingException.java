package com.carlosoliveira.equipment_rental.modules.rental.application.exceptions;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException() {
        super("Payment processing failed");
    }
}
