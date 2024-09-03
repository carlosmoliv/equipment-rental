package com.carlosoliveira.equipment_rental.modules.auth.authentication.exceptions;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
