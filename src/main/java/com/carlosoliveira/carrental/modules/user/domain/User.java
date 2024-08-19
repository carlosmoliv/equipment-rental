package com.carlosoliveira.carrental.modules.user.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
