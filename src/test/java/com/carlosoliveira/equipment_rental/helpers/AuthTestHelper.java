package com.carlosoliveira.equipment_rental.helpers;

import com.carlosoliveira.equipment_rental.modules.iam.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.enums.Role;
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

    @Autowired
    private UserTestHelper userTestHelper;

    Faker faker = new Faker();

    public HttpHeaders getHeadersWithAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAccessToken());
        return headers;
    }

    private String getAccessToken() {
        String password = faker.internet().password();
        User adminUser = userTestHelper.createUser(Role.ADMIN, password);
        return signIn(adminUser.getEmail(), password);
    }

    private String signIn(String email, String password) {
        SignInDto signInDto = new SignInDto(email, password);
        ResponseEntity<String> signInResponse = restTemplate.postForEntity("/api/authentication/sign-in", signInDto, String.class);
        return signInResponse.getBody();
    }
}
