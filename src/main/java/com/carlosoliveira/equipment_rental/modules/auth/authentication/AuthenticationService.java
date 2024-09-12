package com.carlosoliveira.equipment_rental.modules.auth.authentication;

import com.carlosoliveira.equipment_rental.modules.auth.authentication.exceptions.EmailAlreadyInUseException;
import com.carlosoliveira.equipment_rental.modules.auth.authentication.inputs.SignUpInput;
import com.carlosoliveira.equipment_rental.modules.auth.authentication.mappers.UserMapper;
import com.carlosoliveira.equipment_rental.modules.user.application.ports.UserRepository;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.factories.UserFactory;
import com.carlosoliveira.equipment_rental.modules.user.infra.postgres.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFactory userFactory;
    private final UserMapper userMapper;

    public void signUp(SignUpInput signUpInput) {
        Optional<UserEntity> userExists = userRepository.findByEmail(signUpInput.email());
        if (userExists.isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }
        User user = userFactory.createUser(signUpInput, passwordEncoder);
        UserEntity userEntity = userMapper.toPersistence(user);
        userRepository.save(userEntity);
    }
}
