package com.carlosoliveira.equipment_rental.modules.rental;

import com.carlosoliveira.equipment_rental.helpers.AuthTestHelper;
import com.carlosoliveira.equipment_rental.helpers.EquipmentTestHelper;
import com.carlosoliveira.equipment_rental.helpers.UserTestHelper;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.presenters.dtos.CreateRentalDto;
import com.carlosoliveira.equipment_rental.modules.user.domain.enums.Role;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentalIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EquipmentTestHelper equipmentTestHelper;

    @Autowired
    private UserTestHelper userTestHelper;

    @Autowired
    private AuthTestHelper authTestHelper;

    CreateRentalDto createRentalDto;
    HttpHeaders authHeaders;
    Long equipmentId;
    Long customerId;

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("stripe.secret-key", () -> "test-secret-key");
    }

    @BeforeAll
    void setup() {
        authHeaders = authTestHelper.getHeadersWithAuth();
        equipmentId = equipmentTestHelper.createEquipment();
        customerId = userTestHelper.createUser(Role.CUSTOMER).getId();
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        createRentalDto = new CreateRentalDto(customerId, equipmentId, startDate, startDate.plusHours(5));
    }

    @Test
    void rental_is_created_when_valid_data_is_provided() {
        HttpEntity<CreateRentalDto> httpEntity = new HttpEntity<>(createRentalDto, authHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/rentals", httpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void return_rental_succeeds_when_valid() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        CreateRentalDto createRentalDto = new CreateRentalDto(
                customerId,
                equipmentId,
                startDate,
                startDate.plusHours(5)
        );
        ResponseEntity<Rental> createResponse = restTemplate.postForEntity(
                "/api/rentals",
                new HttpEntity<>(createRentalDto, authHeaders),
                Rental.class
        );

        Long rentalId = createResponse.getBody().getId();
        HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders);

        // Act
        ResponseEntity<Rental> returnResponse = restTemplate.postForEntity(
                "/api/rentals/" + rentalId + "/return",
                httpEntity,
                Rental.class
        );

        // Assert
        assertThat(returnResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
