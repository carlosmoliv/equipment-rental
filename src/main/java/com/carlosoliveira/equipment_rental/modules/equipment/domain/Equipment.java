package com.carlosoliveira.equipment_rental.modules.equipment.domain;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private BigDecimal hourlyRate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private EquipmentCategory category;

    @ToString.Exclude
    @OneToMany(mappedBy = "equipment")
    private List<Rental> rentals;

    @Column(nullable = false)
    private BigDecimal lateFeeRate = BigDecimal.ZERO;

    public BigDecimal calculateCost(long durationInHours) {
        BigDecimal rate = this.getHourlyRate();
        BigDecimal totalCost = rate.multiply(BigDecimal.valueOf(durationInHours));
        if (durationInHours > 48) {
            totalCost = totalCost.multiply(BigDecimal.valueOf(0.9));
        }
        return totalCost;
    }
}
