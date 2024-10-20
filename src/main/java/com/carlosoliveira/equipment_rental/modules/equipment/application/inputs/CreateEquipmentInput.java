package com.carlosoliveira.equipment_rental.modules.equipment.application.inputs;

import java.math.BigDecimal;

public record CreateEquipmentInput(
        String name,
        String description,
        BigDecimal pricePerDay,
        boolean available,
        Long categoryId
) {
}
