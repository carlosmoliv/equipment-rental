package com.carlosoliveira.equipment_rental.modules.iam.hashing;

import com.carlosoliveira.equipment_rental.modules.iam.services.HashingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BcryptServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private HashingService bcryptService;

    @Test
    void should_encode_password() {
        // Arrange
        String rawPassword = "password";
        String encodedPassword = "encoded_password";
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // Act
        String result = bcryptService.encode(rawPassword);

        // Assert
        assertEquals(encodedPassword, result);
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void should_match_password() {
        // Arrange
        String rawPassword = "password";
        String encodedPassword = "encoded_password";
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // Act
        boolean result = bcryptService.matches(rawPassword, encodedPassword);

        // Assert
        assertTrue(result);
        verify(passwordEncoder).matches(rawPassword, encodedPassword);
    }
}
