package com.carlosoliveira.equipment_rental.modules.equipmentCategory.application;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.application.inputs.CreateEquipmentCategoryInput;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.infra.jpa.repositories.EquipmentCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EquipmentCategoryService {

    private final EquipmentCategoryRepository equipmentCategoryRepository;

    public void create(CreateEquipmentCategoryInput input) {
        EquipmentCategory category = EquipmentCategory.builder()
                .name(input.name())
                .build();

        equipmentCategoryRepository.save(category);
    }
}
