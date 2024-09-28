package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignUpDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignInInput;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignUpInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private SignInDto signInDto;


    @BeforeEach
    public void init() {
        Faker faker = new Faker();
        String password = faker.internet().password();
        signUpDto = new SignUpDto(
                faker.name().firstName(),
                faker.name().lastName(),
                "any_email@email.com",
                faker.phoneNumber().toString(),
                password,
                password
        );
        signInDto = new SignInDto("valid.email@example.com", password);
    }


    @Nested
    class SignUpTests {
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

    @Nested
    class LoginTests {

        @Test
        void login_returns_token_when_credentials_are_valid() throws Exception {
            // Arrange
            String token = "some-jwt-token";
            when(authenticationService.login(any(SignInInput.class))).thenReturn(token);

            // Act
            ResultActions response = mockMvc.perform(post("/api/authentication/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signInDto)));

            // Assert
            response.andExpect(status().isOk())
                    .andExpect(content().string(token));
        }
    }
}
