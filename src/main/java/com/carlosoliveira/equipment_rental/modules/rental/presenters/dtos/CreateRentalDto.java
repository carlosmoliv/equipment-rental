package com.carlosoliveira.equipment_rental.modules.rental.presenters.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateRentalDto(
        @NotNull
        Long userId,

        @NotNull
        Long equipmentId,

        @NotNull
        LocalDateTime startDate,

        @NotNull
        LocalDateTime endDate
) {
}
