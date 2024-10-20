package com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.repositories;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
