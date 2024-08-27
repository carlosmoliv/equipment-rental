package com.carlosoliveira.carrental.modules.user.domain.factories;

import com.carlosoliveira.carrental.modules.auth.authentication.inputs.SignUpInput;
import com.carlosoliveira.carrental.modules.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

    public User createUser(SignUpInput signUpInput, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(signUpInput.email())
                .firstName(signUpInput.firstName())
                .lastName(signUpInput.lastName())
                .username(signUpInput.username())
                .password(passwordEncoder.encode(signUpInput.password()))
                .build();
    }
}
