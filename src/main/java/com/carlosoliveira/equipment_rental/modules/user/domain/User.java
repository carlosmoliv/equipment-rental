package com.carlosoliveira.equipment_rental.modules.user.domain;

import com.carlosoliveira.equipment_rental.modules.user.domain.enums.Role;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
}
