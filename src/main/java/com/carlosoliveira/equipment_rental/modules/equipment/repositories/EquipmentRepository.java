package com.carlosoliveira.equipment_rental.modules.equipment.repositories;

import com.carlosoliveira.equipment_rental.modules.equipment.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
