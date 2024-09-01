package com.carlosoliveira.carrental.modules.auth.authentication;

import com.carlosoliveira.carrental.modules.auth.authentication.dtos.SignUpDto;
import com.carlosoliveira.carrental.modules.auth.authentication.inputs.SignUpInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @MockBean
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController sut;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SignUpDto signUpDto;


    @BeforeEach
    public void init() {
        Faker faker = new Faker();
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
    void sign_up_succeeds_when_user_does_not_exists() throws Exception {
        // Arrange
        doNothing().when(authenticationService).signUp(any(SignUpInput.class));

        // Act
        ResultActions response = mockMvc.perform(post("/api/authentication/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto)));

        // Assert
        response.andExpect(status().isCreated());
    }
}
