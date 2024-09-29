package com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories;

import com.carlosoliveira.equipment_rental.modules.user.application.ports.UserRepository;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.entities.UserEntity;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.mappers.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaUserRepository implements UserRepository {

    private final EntityManager entityManager;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        String jpql = "SELECT u FROM UserEntity u WHERE u.email = :email";
        UserEntity userEntity = entityManager.createQuery(jpql, UserEntity.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(userEntity).map(userMapper::toDomain);
    }

    @Transactional
    @Override
    public void save(User user) {
        UserEntity userEntity = userMapper.toPersistence(user);
        if (user.getId() == null) {
            entityManager.persist(userEntity);
        } else {
            entityManager.merge(userEntity);
        }
    }
}
