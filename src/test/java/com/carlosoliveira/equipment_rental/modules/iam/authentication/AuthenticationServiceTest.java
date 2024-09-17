package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.exceptions.EmailAlreadyInUseException;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignUpInput;
import com.carlosoliveira.equipment_rental.modules.iam.ports.HashingService;
import com.carlosoliveira.equipment_rental.modules.user.application.ports.UserRepository;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.factories.UserFactory;
import com.carlosoliveira.equipment_rental.modules.user.infra.postgres.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HashingService hashingService;

    @InjectMocks
    private AuthenticationService sut;

    private SignUpInput signUpInput;

    @BeforeEach
    void setUp() {
        UserFactory userFactory = new UserFactory();
        sut = new AuthenticationService(userRepository, hashingService, userFactory);
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
