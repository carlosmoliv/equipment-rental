package com.carlosoliveira.equipment_rental.modules.auth.authentication.mappers;

import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.infra.postgres.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserEntity toPersistence(User user) {
        return UserEntity.builder()
                 .id(user.getId())
                 .username(user.getUsername())
                 .password(user.getPassword())
                 .email(user.getEmail())
                 .firstName(user.getFirstName())
                 .lastName(user.getLastName())
                 .phoneNumber(user.getPhoneNumber())
                 .build();
    }
}
