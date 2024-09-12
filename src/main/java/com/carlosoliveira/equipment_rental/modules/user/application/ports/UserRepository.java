package com.carlosoliveira.equipment_rental.modules.user.application.ports;

import com.carlosoliveira.equipment_rental.modules.user.infra.postgres.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
