package com.carlosoliveira.equipment_rental.modules.iam.token;

import static org.junit.jupiter.api.Assertions.*;

import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        String secretKey = "414b1e7fe1ef832cd7196281557014f704bef678dbfa1c968129fcf97cb12555";
        long expirationTime = 86400000;
        jwtTokenProvider = new JwtTokenProvider(secretKey, expirationTime);
    }

    @Test
    void should_generate_token() {
        // Arrange
        when(user.getEmail()).thenReturn("test@example.com");

        // Act
        String token = jwtTokenProvider.generate(user);

        // Assert
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
    }

    @Test
    void should_validate_token() {
        // Arrange
        String validToken = jwtTokenProvider.generate(user);

        // Act
        boolean isValid = jwtTokenProvider.validate(validToken);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void should_return_email_from_token() {
        // Arrange
        when(user.getEmail()).thenReturn("test@example.com");
        String token = jwtTokenProvider.generate(user);

        // Act
        String email = jwtTokenProvider.getEmailFromToken(token);

        // Assert
        assertEquals("test@example.com", email);
    }

    @Test
    void should_return_false_for_invalid_token() {
        // Act
        boolean isValid = jwtTokenProvider.validate("invalid.token.here");

        // Assert
        assertFalse(isValid);
    }

//    @Test
//    void should_throw_exception_for_invalid_claims() {
//        // Arrange
//        String invalidToken = "invalid.token.here";
//
//        // Act & Assert
//        assertThrows(Exception.class, () -> jwtTokenProvider.extractClaim(invalidToken, Claims::getSubject));
//    }
}
