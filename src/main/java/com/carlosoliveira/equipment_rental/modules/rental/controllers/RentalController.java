package com.carlosoliveira.equipment_rental.modules.rental.controllers;

import com.carlosoliveira.equipment_rental.modules.rental.services.RentalService;
import com.carlosoliveira.equipment_rental.modules.rental.entities.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.dtos.CreateRentalDto;
import com.carlosoliveira.equipment_rental.modules.rental.dtos.PayRentalDto;
import com.carlosoliveira.equipment_rental.modules.rental.dtos.PaymentResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
@RequestMapping("/api/rentals")
@RestController()
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<Rental> create(@Valid @RequestBody CreateRentalDto dto) {
        Rental rental = this.rentalService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @PostMapping("{rentalId}/return")
    public ResponseEntity<Rental> returnRental(@Valid @PathVariable Long rentalId) {
        Rental rental = rentalService.returnRental(rentalId);
        return ResponseEntity.ok(rental);
    }

    @PostMapping("/{rentalId}/pay")
    public ResponseEntity<PaymentResponseDto> payRental(@PathVariable Long rentalId, @RequestBody PayRentalDto dto) {
        PaymentResponseDto paymentResponseDto = rentalService.payRental(rentalId, dto);
        return ResponseEntity.ok(paymentResponseDto);
    }
}
