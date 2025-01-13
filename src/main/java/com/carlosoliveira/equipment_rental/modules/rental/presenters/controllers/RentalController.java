package com.carlosoliveira.equipment_rental.modules.rental.presenters.controllers;

import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.CreateRentalInput;
import com.carlosoliveira.equipment_rental.modules.rental.application.RentalService;
import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PayRentalInput;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.presenters.dtos.CreateRentalDto;
import jakarta.validation.Valid;
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
    public ResponseEntity<Void> create(@Valid @RequestBody CreateRentalDto dto) {
        CreateRentalInput input = new CreateRentalInput(
                dto.userId(),
                dto.equipmentId(),
                dto.startDate(),
                dto.endDate()
        );
        this.rentalService.create(input);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("{rentalId}/return")
    public ResponseEntity<Rental> returnRental(@Valid @PathVariable Long rentalId) {
        Rental rental = rentalService.returnRental(rentalId);
        return ResponseEntity.ok(rental);
    }

    @PostMapping("/{rentalId}/pay")
    public ResponseEntity<Void> payRental(@PathVariable Long rentalId) {
        PayRentalInput input = new PayRentalInput(rentalId);
        rentalService.payRental(input);
        return ResponseEntity.ok().build();
    }
}
