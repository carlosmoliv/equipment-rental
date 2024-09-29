package com.carlosoliveira.equipment_rental.modules.rental.infra.entities.jpa;

import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.entities.EquipmentEntity;
import com.carlosoliveira.equipment_rental.modules.rental.domain.enums.RentalStatus;
import com.carlosoliveira.equipment_rental.modules.user.infra.postgres.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "rentals")
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private EquipmentEntity equipment;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    private RentalStatus status;
}

