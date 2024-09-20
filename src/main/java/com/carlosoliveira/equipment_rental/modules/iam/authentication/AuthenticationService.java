package com.carlosoliveira.equipment_rental.modules.iam.authentication;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.exceptions.EmailAlreadyInUseException;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignInInput;
import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignUpInput;
import com.carlosoliveira.equipment_rental.modules.iam.ports.HashingService;
import com.carlosoliveira.equipment_rental.modules.iam.ports.TokenService;
import com.carlosoliveira.equipment_rental.modules.user.application.ports.UserRepository;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.factories.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final UserFactory userFactory;
    private final TokenService tokenService;

    public void signUp(SignUpInput signUpInput) {
        Optional<User> userExists = userRepository.findByEmail(signUpInput.email());
        if (userExists.isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }
        User user = userFactory.createUser(signUpInput, hashingService);
        userRepository.save(user);
    }


    public String login(SignInInput signInInput) {
        User user = userRepository.findByEmail(signInInput.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        if (!hashingService.matches(signInInput.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials.");
        }

        return tokenService.generate(user);
    }
}
