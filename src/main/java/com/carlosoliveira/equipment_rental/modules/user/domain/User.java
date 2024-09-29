package com.carlosoliveira.equipment_rental.modules.user.domain;

import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import com.carlosoliveira.equipment_rental.modules.user.domain.enums.Role;
import lombok.*;

import java.util.List;

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
//    private List<Rental> rentals;
}
