package com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.entities;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.infra.jpa.entities.EquipmentCategoryEntity;
import com.carlosoliveira.equipment_rental.modules.rental.infra.entities.jpa.RentalEntity;
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
public class EquipmentEntity {

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private EquipmentCategoryEntity category;

    @OneToMany(mappedBy = "equipment")
    private List<RentalEntity> rentals;
}
