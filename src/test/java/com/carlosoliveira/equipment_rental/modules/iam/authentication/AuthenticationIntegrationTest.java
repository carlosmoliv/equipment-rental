package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignUpDto;
import com.carlosoliveira.equipment_rental.modules.user.application.ports.UserRepository;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    private SignUpDto signUpDto;

    @Autowired
    private TestRestTemplate restTemplate;

    Faker faker = new Faker();

    @BeforeEach
    public void init() {
        String password = faker.internet().password();
        signUpDto = new SignUpDto(
                faker.name().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                "any_email@email.com",
                faker.phoneNumber().toString(),
                password,
                password
        );
    }

    @Test
    void connection_established()  {
        Assertions.assertThat(postgres.isCreated()).isTrue();
        Assertions.assertThat(postgres.isRunning()).isTrue();
    }

    @Nested
    class SignUpTests {

        @Test
        void sign_up_succeeds() {
            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/authentication/sign-up", signUpDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertTrue(userRepository.findByEmail(signUpDto.email()).isPresent());
        }
    }
}