 package com.carlosoliveira.equipment_rental.helpers;

import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserTestHelper {

    @Autowired
    private UserRepository userRepository;

    private final Faker faker = new Faker();

    public Long createUser() {
        User user = User.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .phoneNumber(faker.phoneNumber().cellPhone())
                .build();
        User persistedUser = userRepository.save(user);
        System.out.println(persistedUser);
        return persistedUser.getId();
    }
}
