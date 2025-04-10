package com.carlosoliveira.equipment_rental.modules.equipmentCategory.repositories;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.entities.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {
}
