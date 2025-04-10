package com.carlosoliveira.equipment_rental.modules.equipmentCategory.controllers;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.services.EquipmentCategoryService;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.entities.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.dtos.CreateEquipmentCategoryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class EquipmentCategoryController {

    private final EquipmentCategoryService categoryService;

    @PostMapping()
    public ResponseEntity<Long> create(@Valid @RequestBody CreateEquipmentCategoryDto dto) {
        Long categoryId = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
    }

    @GetMapping()
    public ResponseEntity<List<EquipmentCategory>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }
}
