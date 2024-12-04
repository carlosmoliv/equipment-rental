package com.carlosoliveira.equipment_rental.helpers;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignUpDto;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthTestHelper {

    @Autowired
    private TestRestTemplate restTemplate;
    private final Faker faker = new Faker();

    public HttpHeaders getHeadersWithAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        return headers;
    }

    private String getAccessToken() {
        String password = "pass123";
        String email = faker.internet().emailAddress();
        signUp(email, password);
        return signIn(email, password);
    }

    private void signUp(String email, String password) {
        SignUpDto signUpDto = new SignUpDto(faker.name().firstName(), faker.name().lastName(), email, faker.phoneNumber().toString(), password, password);
        restTemplate.postForEntity("/api/authentication/sign-up", signUpDto, String.class);
    }

    private String signIn(String email, String password) {
        SignInDto signInDto = new SignInDto(email, password);
        ResponseEntity<String> signInResponse = restTemplate.postForEntity("/api/authentication/sign-in", signInDto, String.class);
        return signInResponse.getBody();
    }
}
