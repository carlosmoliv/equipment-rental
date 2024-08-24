package com.carlosoliveira.carrental.modules.auth.authentication;

import com.carlosoliveira.carrental.modules.auth.authentication.inputs.SignUpInput;
import com.carlosoliveira.carrental.modules.user.application.ports.UserRepository;
import com.carlosoliveira.carrental.modules.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public void signUp(SignUpInput signUpInput) {
        Optional<User> userExists = userRepository.findByEmail(signUpInput.email());
        if (userExists.isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setEmail(signUpInput.email());
        user.setFirstName(signUpInput.firstName());
        user.setLastName(signUpInput.lastName());
        user.setUsername(signUpInput.username());
        user.setPassword(passwordEncoder.encode(signUpInput.password()));
        userRepository.save(user);
    }
}
