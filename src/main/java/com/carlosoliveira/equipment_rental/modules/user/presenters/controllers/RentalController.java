package com.carlosoliveira.equipment_rental.modules.user.presenters.controllers;

import com.carlosoliveira.equipment_rental.modules.rental.application.CreateRentalInput;
import com.carlosoliveira.equipment_rental.modules.rental.application.RentalService;
import com.carlosoliveira.equipment_rental.modules.user.presenters.dtos.CreateRentalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/rentals")
@RestController()
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateRentalDto dto) {
        CreateRentalInput input = new CreateRentalInput(
                dto.userId(),
                dto.equipmentId(),
                dto.startDate(),
                dto.endDate()
        );
        this.rentalService.create(input);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
