package com.carlosoliveira.equipment_rental.modules.user.infra.postgres.mappers;

import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.infra.postgres.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserEntity toPersistence(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                 .role(user.getRole())
                .build();
    }

    public User toDomain(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .phoneNumber(userEntity.getPhoneNumber())
                .role(userEntity.getRole())
                .build();
    }
}
