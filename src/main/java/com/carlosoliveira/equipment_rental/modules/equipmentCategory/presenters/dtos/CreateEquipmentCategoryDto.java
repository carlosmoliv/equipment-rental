package com.carlosoliveira.equipment_rental.modules.equipmentCategory.presenters.dtos;

import jakarta.validation.constraints.NotBlank;
public record CreateEquipmentCategoryDto (
        @NotBlank(message = "Name is required")
        String name
) {
}
