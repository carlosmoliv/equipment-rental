package com.carlosoliveira.equipment_rental.modules.rental.domain;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.rental.domain.enums.RentalStatus;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    @Column()
    private LocalDateTime returnDate;

    @Column()
    private BigDecimal lateFees;

    public BigDecimal calculateLateFees() {
        if (this.getStatus() != RentalStatus.COMPLETED) {
            return BigDecimal.ZERO;
        }
        LocalDateTime returnDate = LocalDateTime.now();
        if (returnDate.isBefore(this.getEndDate())) {
            return BigDecimal.ZERO;
        }
        long lateHours = Duration.between(this.getEndDate(), returnDate).toHours();
        return BigDecimal.valueOf(lateHours).multiply(this.getEquipment().getLateFeeRate());
    }
}

