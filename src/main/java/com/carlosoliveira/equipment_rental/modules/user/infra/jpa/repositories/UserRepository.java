package com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories;

import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
