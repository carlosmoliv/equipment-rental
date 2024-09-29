package com.carlosoliveira.equipment_rental.modules.equipmentCategory.infra.jpa.entities;

import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.entities.EquipmentEntity;
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
@Table(name = "equipment_categories")
public class EquipmentCategoryEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<EquipmentEntity> equipments;
}
