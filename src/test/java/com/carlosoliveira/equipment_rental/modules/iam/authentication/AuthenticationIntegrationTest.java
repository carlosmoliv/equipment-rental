package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignUpDto;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories.UserRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AuthenticationIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private SignUpDto signUpDto;
    private SignInDto signInDto;
    Faker faker = new Faker();

    @BeforeEach
    public void init() {
        String password = faker.internet().password();
        signUpDto = new SignUpDto(
                faker.name().firstName(),
                faker.name().lastName(),
                "any_email@email.com",
                faker.phoneNumber().toString(),
                password,
                password
        );

        signInDto = new SignInDto(
                signUpDto.email(),
                signUpDto.password()
        );
    }

    @Test
    void connection_established()  {
        Assertions.assertThat(postgres.isCreated()).isTrue();
        Assertions.assertThat(postgres.isRunning()).isTrue();
    }

    @Nested
    class SignUpTests {
        static Stream<SignUpDto> invalidSignUpDtos() {
            return Stream.of(
                    new SignUpDto( null, "Doe", "john@example.com", "1234567890", "password", "password"),
                    new SignUpDto( "John", null, "john@example.com", "1234567890", "password", "password"),
                    new SignUpDto( "John", "Doe", null, "1234567890", "password", "password"),
                    new SignUpDto( "John", "Doe", "john@example.com", null, "password", "password"),
                    new SignUpDto( "John", "Doe", "john@example.com", "1234567890", null, "password"),
                    new SignUpDto( "John", "Doe", "john@example.com", "1234567890", "password", null)
            );
        }

        @Test
        void sign_up_succeeds() {
            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-up", signUpDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertTrue(userRepository.findByEmail(signUpDto.email()).isPresent());
        }

        @ParameterizedTest
        @MethodSource("invalidSignUpDtos")
        void signUp_fails_with_missing_fields(SignUpDto invalidSignUpDto) {
            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-up", invalidSignUpDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    class LoginTests {

        @Test
        void login_succeeds_with_valid_credentials() {
            // Arrange
            restTemplate.postForEntity("/api/authentication/sign-up", signUpDto, String.class);

            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-in", signInDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isNotNull();
        }

        @Test
        void login_fails_with_invalid_email() {
            // Arrange
            SignInDto invalidSignInDto = new SignInDto("invalid_email_format", signInDto.password());

            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-in", invalidSignInDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void login_fails_with_missing_password() {
            // Arrange
            SignInDto invalidSignInDto = new SignInDto(signInDto.email(), null);

            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-in", invalidSignInDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void login_fails_with_short_password() {
            // Arrange
            SignInDto invalidSignInDto = new SignInDto(signInDto.email(),"short");

            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-in", invalidSignInDto, String.class);

             // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        void sign_up_fails_when_passwords_do_not_match() {
            // Arrange
            SignUpDto invalidSignUpDto = new SignUpDto(
                    "firstName",
                    "lastName",
                    "email@example.com",
                    "123456789",
                    "password123",
                    "differentPassword"
            );

            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-up", invalidSignUpDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }
}
