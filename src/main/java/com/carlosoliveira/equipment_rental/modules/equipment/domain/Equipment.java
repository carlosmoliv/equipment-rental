package com.carlosoliveira.equipment_rental.modules.equipment.domain;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Equipment {

    private Long id;
    private String name;
    private String description;
    private BigDecimal pricePerDay;
    private boolean available;
    private EquipmentCategory category;
    private List<Rental> rentals;
}
