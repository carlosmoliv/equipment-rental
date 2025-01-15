package com.carlosoliveira.equipment_rental.modules.equipment.presenters.controllers;

import com.carlosoliveira.equipment_rental.modules.equipment.application.EquipmentService;
import com.carlosoliveira.equipment_rental.modules.equipment.application.inputs.CreateEquipmentInput;
import com.carlosoliveira.equipment_rental.modules.equipment.presenters.dtos.CreateEquipmentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipments")
public class EquipmentController {
    private final EquipmentService equipmentService;

    @PostMapping()
    public ResponseEntity<Long> signUp(@Valid @RequestBody CreateEquipmentDto createEquipmentDto) {
        CreateEquipmentInput createEquipmentInput = new CreateEquipmentInput(
                createEquipmentDto.name(),
                createEquipmentDto.description(),
                createEquipmentDto.pricePerDay(),
                createEquipmentDto.available(),
                createEquipmentDto.categoryId(),
                createEquipmentDto.lateFeeRate(),
                createEquipmentDto.hourlyRate()
        );
        Long equipmentId = equipmentService.create(createEquipmentInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentId);
    }
}
