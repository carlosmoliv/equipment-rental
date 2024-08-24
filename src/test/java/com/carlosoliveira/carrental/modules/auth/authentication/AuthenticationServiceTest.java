package com.carlosoliveira.carrental.modules.auth.authentication;

import com.carlosoliveira.carrental.modules.auth.authentication.exceptions.EmailAlreadyInUseException;
import com.carlosoliveira.carrental.modules.auth.authentication.inputs.SignUpInput;
import com.carlosoliveira.carrental.modules.user.application.ports.UserRepository;
import com.carlosoliveira.carrental.modules.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService sut;

    private SignUpInput signUpInput;

    @BeforeEach
    void setUp() {
        signUpInput = new SignUpInput(
                "any_username",
                "any first name",
                "any last name",
                "password",
                "test@example.com",
                "+5522999999999"
        );
    }

    @Test
    void creates_a_new_user() {
        // Arrange
        when(passwordEncoder.encode(signUpInput.password())).thenReturn("hashed_password");

        // Act
        sut.signUp(signUpInput);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void fails_when_email_already_in_use() {
        // Arrange
        User existingUser = new User();
        when(userRepository.findByEmail(signUpInput.email())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(EmailAlreadyInUseException.class, () -> sut.signUp(signUpInput));
    }
}
