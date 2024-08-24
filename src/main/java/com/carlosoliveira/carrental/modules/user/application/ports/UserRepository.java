package com.carlosoliveira.carrental.modules.user.application.ports;

import com.carlosoliveira.carrental.modules.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByEmail(String email);
}
