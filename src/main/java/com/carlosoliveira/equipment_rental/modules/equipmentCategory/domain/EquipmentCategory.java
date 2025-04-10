package com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain;

import com.carlosoliveira.equipment_rental.modules.equipment.entities.Equipment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "equipment_categories")
public class EquipmentCategory {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<Equipment> equipments;
}
