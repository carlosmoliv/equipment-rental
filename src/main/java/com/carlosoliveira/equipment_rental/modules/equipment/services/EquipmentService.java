package com.carlosoliveira.equipment_rental.modules.equipment.services;

import com.carlosoliveira.equipment_rental.modules.equipment.dtos.CreateEquipmentDto;
import com.carlosoliveira.equipment_rental.modules.equipment.repositories.EquipmentRepository;
import com.carlosoliveira.equipment_rental.modules.equipment.entities.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.repositories.EquipmentCategoryRepository;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.entities.EquipmentCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository categoryEquipmentRepository;

    public Long create(CreateEquipmentDto dto) {
        Optional<EquipmentCategory> category = categoryEquipmentRepository.findById(dto.categoryId());
        if (category.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found.");
        }
        Equipment equipment = Equipment.builder()
                .name(dto.name())
                .description(dto.description())
                .pricePerDay(dto.pricePerDay())
                .available(dto.available())
                .category(category.get())
                .lateFeeRate(dto.lateFeeRate())
                .hourlyRate(dto.hourlyRate())
                .build();
        return equipmentRepository.save(equipment).getId();
    }
}
