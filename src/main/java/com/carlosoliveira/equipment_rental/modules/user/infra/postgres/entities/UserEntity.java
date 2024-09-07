 package com.carlosoliveira.equipment_rental.modules.user.infra.postgres.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column()
    private String username;

    @Column()
    private String firstName;

    @Column()
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column()
    private String password;

    @Column()
    private String phoneNumber;

    @CreatedDate
    @Column()
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column()
    private LocalDateTime updatedAt;
}
