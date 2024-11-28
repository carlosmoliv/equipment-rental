package com.carlosoliveira.equipment_rental.modules.equipment.domain;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "equipment")
    private List<Rental> rentals;

    @Column(nullable = false)
    private BigDecimal lateFeeRate;
}
