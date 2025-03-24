package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.exceptions.EmailAlreadyInUseException;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignInInput;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignUpInput;
import com.carlosoliveira.equipment_rental.modules.iam.ports.HashingService;
import com.carlosoliveira.equipment_rental.modules.iam.ports.TokenService;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.enums.Role;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    public void signUp(SignUpInput signUpInput) {
        Optional<User> userExists = userRepository.findByEmail(signUpInput.email());
        if (userExists.isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }
        User user = User.builder()
                .email(signUpInput.email())
                .firstName(signUpInput.firstName())
                .lastName(signUpInput.lastName())
                .password(hashingService.encode(signUpInput.password()))
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);
    }

    public String login(SignInInput signInInput) {
        Optional<User> user = userRepository.findByEmail(signInInput.email());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.");
        }
        if (!hashingService.matches(signInInput.password(), user.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.");
        }
        return tokenService.generate(user.get());
    }
}
