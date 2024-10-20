package com.carlosoliveira.equipment_rental.modules.equipmentCategory.infra.jpa.repositories;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {
}
