package com.carlosoliveira.equipment_rental.modules.iam.ports;

import com.carlosoliveira.equipment_rental.modules.user.domain.User;

public interface TokenService {
    String generate(User user);
}
