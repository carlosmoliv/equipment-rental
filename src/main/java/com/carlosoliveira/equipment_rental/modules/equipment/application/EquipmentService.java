package com.carlosoliveira.equipment_rental.modules.equipment.application;

import com.carlosoliveira.equipment_rental.modules.equipment.application.inputs.CreateEquipmentInput;
import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.repositories.EquipmentRepository;
import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.infra.jpa.repositories.EquipmentCategoryRepository;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository categoryEquipmentRepository;

    public void create(CreateEquipmentInput input) {
        Optional<EquipmentCategory> category = categoryEquipmentRepository.findById(input.categoryId());
        if (category.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Equipment equipment = Equipment.builder()
                .name(input.name())
                .description(input.description())
                .pricePerDay(input.pricePerDay())
                .available(input.available())
                .category(category.get())
                .lateFeeRate(input.lateFeeRate())
                .hourlyRate(input.hourlyRate())
                .build();

        equipmentRepository.save(equipment);
    }
}
