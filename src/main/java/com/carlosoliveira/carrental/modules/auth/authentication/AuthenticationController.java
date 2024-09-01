package com.carlosoliveira.carrental.modules.auth.authentication;

import com.carlosoliveira.carrental.modules.auth.authentication.dtos.SignUpDto;
import com.carlosoliveira.carrental.modules.auth.authentication.inputs.SignUpInput;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication/sign-up")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody SignUpDto signUpDto) {
        SignUpInput signUpInput = new SignUpInput(
                signUpDto.username(),
                signUpDto.firstName(),
                signUpDto.lastName(),
                signUpDto.password(),
                signUpDto.email(),
                signUpDto.email()
        );
        authenticationService.signUp(signUpInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
