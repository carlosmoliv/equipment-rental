package com.carlosoliveira.equipment_rental.modules.equipmentCategory.services;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.dtos.CreateEquipmentCategoryDto;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.entities.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.repositories.EquipmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EquipmentCategoryService {

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    public Long create(CreateEquipmentCategoryDto dto) {
        EquipmentCategory category = EquipmentCategory.builder()
                .name(dto.name())
                .build();
        return equipmentCategoryRepository.save(category).getId();
    }

    public List<EquipmentCategory> findAll() {
        return equipmentCategoryRepository.findAll();
    }
}
