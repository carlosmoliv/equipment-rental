package com.carlosoliveira.equipment_rental.modules.equipmentCategory.presenters.controllers;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.application.EquipmentCategoryService;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.application.inputs.CreateEquipmentCategoryInput;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.presenters.dtos.CreateEquipmentCategoryDto;
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
    public ResponseEntity<Long> create(@Valid @RequestBody CreateEquipmentCategoryDto createCategoryDto) {
        CreateEquipmentCategoryInput createCategoryInput = new CreateEquipmentCategoryInput(
                createCategoryDto.name()
        );
        Long categoryId = categoryService.create(createCategoryInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryId);
    }

    @GetMapping()
    public ResponseEntity<List<EquipmentCategory>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }
}
