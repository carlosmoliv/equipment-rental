package com.carlosoliveira.equipment_rental.modules.equipment.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateEquipmentDto (
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Description is required")
    String description,

    @NotNull(message = "Price per day is required")
    BigDecimal pricePerDay,

    boolean available,

    @NotNull
    Long categoryId,

    @NotNull(message = "Late fee rate is required")
    BigDecimal lateFeeRate,

    @NotNull(message = "Hourly rate is required")
    BigDecimal hourlyRate
){
}
