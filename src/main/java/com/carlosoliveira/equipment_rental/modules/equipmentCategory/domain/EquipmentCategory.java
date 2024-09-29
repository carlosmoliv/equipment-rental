package com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EquipmentCategory {
    private Long id;
    private String name;
    private List<Equipment> equipmentList;
}
