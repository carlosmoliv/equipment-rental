package com.carlosoliveira.equipment_rental.modules.user.presenters.dtos;

import java.time.LocalDateTime;

public record CreateRentalDto(
        Long userId,
        Long equipmentId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
