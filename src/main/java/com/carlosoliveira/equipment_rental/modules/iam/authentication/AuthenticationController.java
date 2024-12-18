package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.dtos.SignUpDto;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignInInput;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignUpInput;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        SignUpInput signUpInput = new SignUpInput(
                signUpDto.firstName(),
                signUpDto.lastName(),
                signUpDto.password(),
                signUpDto.email(),
                signUpDto.phoneNumber()
        );
        authenticationService.signUp(signUpInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> login(@Valid @RequestBody SignInDto signInDto) {
        SignInInput signInInput = new SignInInput(signInDto.password(), signInDto.email());
        String token = authenticationService.login(signInInput);
        return ResponseEntity.ok(token);
    }
}
