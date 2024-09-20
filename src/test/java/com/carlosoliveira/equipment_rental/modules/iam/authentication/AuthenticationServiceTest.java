package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.exceptions.EmailAlreadyInUseException;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignInInput;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignUpInput;
import com.carlosoliveira.equipment_rental.modules.iam.ports.HashingService;
import com.carlosoliveira.equipment_rental.modules.iam.ports.TokenService;
import com.carlosoliveira.equipment_rental.modules.user.application.ports.UserRepository;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.factories.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashingService hashingService;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationService sut;

    private SignUpInput signUpInput;
    private SignInInput signInInput;
    private User user;


    @BeforeEach
    void setUp() {
        UserFactory userFactory = new UserFactory();
        sut = new AuthenticationService(userRepository, hashingService, userFactory, tokenService);
        signUpInput = new SignUpInput(
                "any_username",
                "any first name",
                "any last name",
                "password",
                "test@example.com",
                "+5522999999999"
        );

        signInInput = new SignInInput(
                "test@example.com",
                "password"
        );

        user = new User();
        user.setEmail(signInInput.email());
        user.setPassword("hashed_password");
    }

    @Nested
    class SignUpTests {
        @Test
        void creates_a_new_user() {
            // Arrange
            String hashedPassword = "hashed_password";
            when(hashingService.encode(signUpInput.password())).thenReturn(hashedPassword);
            when(userRepository.findByEmail(signUpInput.email())).thenReturn(Optional.empty());

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

    @Nested
    class SignInTests {
        @Test
        void returns_token_when_credentials_are_valid() {
            // Arrange
            when(userRepository.findByEmail(signInInput.email())).thenReturn(Optional.of(user));
            when(hashingService.matches(signInInput.password(), user.getPassword())).thenReturn(true);
            when(tokenService.generate(user)).thenReturn("token");

            // Act
            String token = sut.login(signInInput);

            // Assert
            assertEquals("token", token);
            verify(tokenService, times(1)).generate(user);
        }
    }
}
