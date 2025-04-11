 package com.carlosoliveira.equipment_rental.helpers;

import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.domain.enums.Role;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTestHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashingService hashingService;

    private final Faker faker = new Faker();

     public User createUser(Role role) {
         return createUser(role, faker.internet().password());
     }

     public User createUser(Role role, String password) {
        User user = User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(hashingService.encode(password))
                .phoneNumber(faker.phoneNumber().cellPhone())
                .role(role)
                .build();
        return userRepository.save(user);
    }
}
