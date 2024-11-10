package com.carlosoliveira.equipment_rental.modules.equipment;

import com.carlosoliveira.equipment_rental.modules.equipment.presenters.dtos.CreateEquipmentDto;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.presenters.dtos.CreateEquipmentCategoryDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignUpDto;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class EquipmentIntegrationTest {

    Faker faker = new Faker();
    private CreateEquipmentDto createEquipmentDto;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    static void startContainers() {
        postgres.start();
    }

    @AfterAll
    static void stopContainers() {
        postgres.stop();
    }

    @Test
    void connection_established()  {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Nested
    class CreateTests {

        @Test
        void equipment_is_created_when_valid_data_is_provided() {
            // Arrange
            createEquipmentDto = new CreateEquipmentDto(
                    faker.commerce().productName(),
                    faker.lorem().paragraph(3),
                    BigDecimal.valueOf(faker.number().randomDouble(2, 50, 1000)),
                    true,
                    getCategoryId()
            );
            HttpEntity<CreateEquipmentDto> equipmentDto = new HttpEntity<>(createEquipmentDto, getHeadersWithToken());

            // Act
            ResponseEntity<String> response = restTemplate.postForEntity("/api/equipments", equipmentDto, String.class);

            // Assert
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }

    private Long getCategoryId() {
        CreateEquipmentCategoryDto createCategoryDto = new CreateEquipmentCategoryDto("Electronics");
        HttpEntity<CreateEquipmentCategoryDto> categoryRequest = new HttpEntity<>(createCategoryDto, getHeadersWithToken());
        ResponseEntity<String> categoryResponse = restTemplate.postForEntity(
                "/api/categories",
                categoryRequest,
                String.class
        );
        String categoryId = categoryResponse.getBody();
        assert categoryId != null;
        return Long.valueOf(categoryId);
    }

    private HttpHeaders getHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        return headers;
    }

    private String getAccessToken() {
        String pass = "pass123";
        String email = faker.internet().emailAddress();

        SignUpDto signUpDto = new SignUpDto(faker.name().firstName(), faker.name().lastName(), email, faker.phoneNumber().toString(), pass, pass);
        restTemplate.postForEntity("/api/authentication/sign-up", signUpDto, String.class);

        SignInDto signInDto = new SignInDto(email, pass);

        ResponseEntity<String> signInResponse = restTemplate.postForEntity("/api/authentication/sign-in", signInDto, String.class);
        return signInResponse.getBody();
    }
}
