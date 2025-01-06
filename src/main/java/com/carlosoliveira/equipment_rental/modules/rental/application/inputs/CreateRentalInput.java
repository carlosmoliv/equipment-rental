package com.carlosoliveira.equipment_rental.modules.rental.application.inputs;

import java.time.LocalDateTime;

public record CreateRentalInput(
        Long userId,
        Long equipmentId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
