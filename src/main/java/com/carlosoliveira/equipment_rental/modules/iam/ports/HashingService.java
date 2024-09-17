package com.carlosoliveira.equipment_rental.modules.iam.ports;

public interface HashingService {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
