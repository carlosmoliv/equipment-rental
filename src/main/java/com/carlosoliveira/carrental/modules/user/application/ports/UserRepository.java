package com.carlosoliveira.carrental.modules.user.application.ports;

import com.carlosoliveira.carrental.modules.user.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    void save(User user);
    Optional<User> findByEmail(String email);
}
