package com.carlosoliveira.carrental.modules.user.application.ports;

import com.carlosoliveira.carrental.modules.user.domain.User;

public interface UserRepository {
    void save(User user);
}
