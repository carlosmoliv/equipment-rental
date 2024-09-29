package com.carlosoliveira.equipment_rental.modules.rental.domain;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.rental.domain.enums.RentalStatus;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
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
public class Rental {
    private Long id;
    private User user;
    private Equipment equipment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalCost;
    private RentalStatus status;
}
