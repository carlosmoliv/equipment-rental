package com.carlosoliveira.equipment_rental.modules.rental.services;

import com.carlosoliveira.equipment_rental.modules.equipment.entities.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipment.repositories.EquipmentRepository;
import com.carlosoliveira.equipment_rental.modules.payment.services.PaymentGatewayService;
import com.carlosoliveira.equipment_rental.modules.rental.dtos.CreateRentalDto;
import com.carlosoliveira.equipment_rental.modules.rental.dtos.PayRentalDto;
import com.carlosoliveira.equipment_rental.modules.rental.dtos.PaymentDetails;
import com.carlosoliveira.equipment_rental.modules.rental.entities.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.enums.RentalStatus;
import com.carlosoliveira.equipment_rental.modules.rental.repositories.RentalRepository;
import com.carlosoliveira.equipment_rental.modules.rental.dtos.PaymentResponseDto;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories.UserRepository;
import com.stripe.model.PaymentIntent;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final PaymentGatewayService paymentGatewayService;

    public Rental create(CreateRentalDto dto) {
        Equipment equipment = equipmentRepository.findById(dto.equipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        if (!isEquipmentAvailable(equipment, dto.startDate(), dto.endDate())) {
            throw new IllegalStateException("Equipment is not available for the selected dates");
        }

        User user = userRepository.findById(dto.userId())
                .orElseThrow(()  -> new EntityNotFoundException("User not found"));
        if (!canUserRent(user)) {
            throw new IllegalStateException("User has reached the rental limit");
        }

        BigDecimal totalCost = equipment.calculateCost(Duration.between(dto.startDate(), dto.endDate()).toHours());
        Rental rental = Rental.builder()
                .status(RentalStatus.PENDING)
                .equipment(equipment)
                .user(user)
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .totalCost(totalCost)
                .build();

       return rentalRepository.save(rental);
    }

    public Rental returnRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

        if (rental.getStatus() != RentalStatus.ONGOING) {
            throw new IllegalStateException("Only ongoing rentals can be returned");
        }

        rental.setStatus(RentalStatus.COMPLETED);
        rental.setReturnDate(LocalDateTime.now());
        rental.setLateFees(rental.calculateLateFees());
        rentalRepository.save(rental);
        return rental;
    }

    public PaymentResponseDto payRental(Long rentalId, PayRentalDto dto) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Only PENDING rentals can be paid.");
        }

        rental.setStatus(RentalStatus.ONGOING);
        rentalRepository.save(rental);

        PaymentDetails paymentDetails = new PaymentDetails(rental.getTotalCost(), rental.getUser().getEmail(), dto.creditCardToken());
        PaymentIntent paymentIntent =  paymentGatewayService.processPayment(paymentDetails);
        return new PaymentResponseDto(paymentIntent.getId(), paymentIntent.getStatus());
    }

    private boolean isEquipmentAvailable(Equipment equipment, LocalDateTime startDate, LocalDateTime endDate) {
        List<Rental> equipments = rentalRepository.findByEquipmentAndPeriod(equipment, startDate, endDate);
        return equipments.isEmpty();
    }

    private boolean canUserRent(User user) {
        List<Rental> activeRentals = rentalRepository.findByUserAndStatus(user, RentalStatus.ONGOING);
        return activeRentals.size() < 3;
    }
}
