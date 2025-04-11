package com.carlosoliveira.equipment_rental.modules.iam.controllers;

import com.carlosoliveira.equipment_rental.modules.iam.services.AuthenticationService;
import com.carlosoliveira.equipment_rental.modules.iam.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.dtos.SignUpDto;
import com.carlosoliveira.equipment_rental.modules.iam.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.dtos.SignUpDto;
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
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpDto dto) {
        authenticationService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> login(@Valid @RequestBody SignInDto dto) {
        String token = authenticationService.login(dto);
        return ResponseEntity.ok(token);
    }
}
