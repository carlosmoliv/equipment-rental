package com.carlosoliveira.equipment_rental.helpers;

import com.carlosoliveira.equipment_rental.modules.equipment.dtos.CreateEquipmentDto;
import com.carlosoliveira.equipment_rental.modules.equipment.services.EquipmentService;
import com.carlosoliveira.equipment_rental.modules.equipment.application.inputs.CreateEquipmentInput;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.application.EquipmentCategoryService;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.application.inputs.CreateEquipmentCategoryInput;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EquipmentTestHelper {
    @Autowired
    private EquipmentCategoryService equipmentCategoryService;

    @Autowired
    private EquipmentService equipmentService;

    public Long createEquipment() {
        Faker faker = new Faker();
        Long categoryId = equipmentCategoryService.create(new CreateEquipmentCategoryInput("Electronics"));
        CreateEquipmentDto dto = new CreateEquipmentDto(
                faker.commerce().productName(),
                faker.lorem().paragraph(3),
                BigDecimal.valueOf(faker.number().randomDouble(2, 50, 1000)),
                true,
                categoryId,
                BigDecimal.valueOf(faker.number().randomDouble(2, 50, 1000)),
                BigDecimal.valueOf(faker.number().randomDouble(2, 1, 20))
        );
        return equipmentService.create(dto);
    }
}
