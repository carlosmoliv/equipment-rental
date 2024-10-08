package com.carlosoliveira.equipment_rental.modules.user.domain.factories;

import com.carlosoliveira.equipment_rental.modules.iam.authentication.inputs.SignUpInput;
import com.carlosoliveira.equipment_rental.modules.iam.ports.HashingService;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.enums.Role;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

    public User createUser(SignUpInput signUpInput, HashingService hashingService) {
        return User.builder()
                .email(signUpInput.email())
                .firstName(signUpInput.firstName())
                .lastName(signUpInput.lastName())
                .password(hashingService.encode(signUpInput.password()))
                .role(Role.CUSTOMER)
                .build();
    }
}
