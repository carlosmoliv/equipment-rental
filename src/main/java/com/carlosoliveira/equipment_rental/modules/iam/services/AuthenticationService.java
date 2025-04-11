package com.carlosoliveira.equipment_rental.modules.iam.services;

import com.carlosoliveira.equipment_rental.modules.iam.dtos.SignInDto;
import com.carlosoliveira.equipment_rental.modules.iam.dtos.SignUpDto;
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

    public void signUp(SignUpDto dto) {
        Optional<User> userExists = userRepository.findByEmail(dto.email());
        if (userExists.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use.");
        }
        User user = User.builder()
                .email(dto.email())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .password(hashingService.encode(dto.password()))
                .role(Role.CUSTOMER)
                .build();
        userRepository.save(user);
    }

    public String login(SignInDto dto) {
        Optional<User> user = userRepository.findByEmail(dto.email());
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found.");
        }
        if (!hashingService.matches(dto.password(), user.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.");
        }
        return tokenService.generate(user.get());
    }
}
