package com.carlosoliveira.equipment_rental.modules.auth.authentication;

import com.carlosoliveira.equipment_rental.modules.auth.authentication.exceptions.EmailAlreadyInUseException;
import com.carlosoliveira.equipment_rental.modules.auth.authentication.inputs.SignUpInput;
import com.carlosoliveira.equipment_rental.modules.auth.authentication.mappers.UserMapper;
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

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthenticationService sut;

    private SignUpInput signUpInput;

    @BeforeEach
    void setUp() {
        UserFactory userFactory = new UserFactory();
        sut = new AuthenticationService(userRepository, passwordEncoder, userFactory, userMapper);
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
        UserEntity userEntity = new UserEntity();
        when(passwordEncoder.encode(signUpInput.password())).thenReturn(hashedPassword);
        when(userMapper.toPersistence(any(User.class))).thenReturn(userEntity);
        when(userRepository.findByEmail(signUpInput.email())).thenReturn(Optional.empty());

        // Act
        sut.signUp(signUpInput);

        // Assert
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void fails_when_email_already_in_use() {
        // Arrange
        UserEntity existingUser = new UserEntity();
        when(userRepository.findByEmail(signUpInput.email())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(EmailAlreadyInUseException.class, () -> sut.signUp(signUpInput));
    }
}
