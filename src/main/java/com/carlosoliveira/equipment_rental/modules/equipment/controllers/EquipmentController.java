package com.carlosoliveira.equipment_rental.modules.equipment.controllers;

import com.carlosoliveira.equipment_rental.modules.equipment.services.EquipmentService;
import com.carlosoliveira.equipment_rental.modules.equipment.dtos.CreateEquipmentDto;
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
    public ResponseEntity<Long> signUp(@Valid @RequestBody CreateEquipmentDto dto) {
        Long equipmentId = equipmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentId);
    }
}
