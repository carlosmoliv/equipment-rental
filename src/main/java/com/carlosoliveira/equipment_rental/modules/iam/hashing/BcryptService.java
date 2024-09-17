package com.carlosoliveira.equipment_rental.modules.iam.hashing;

import com.carlosoliveira.equipment_rental.modules.iam.ports.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptService implements HashingService {

    private final PasswordEncoder passwordEncoder;

    public BcryptService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
