package com.carlosoliveira.equipment_rental.modules.rental.application;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.repositories.EquipmentRepository;
import com.carlosoliveira.equipment_rental.modules.rental.application.exceptions.PaymentProcessingException;
import com.carlosoliveira.equipment_rental.modules.rental.application.exceptions.PaymentStatusException;
import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.CreateRentalInput;
import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PayRentalInput;
import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PaymentDetails;
import com.carlosoliveira.equipment_rental.modules.payment.application.PaymentGatewayService;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.domain.enums.RentalStatus;
import com.carlosoliveira.equipment_rental.modules.rental.infra.repositories.RentalRepository;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Rental create(CreateRentalInput input) {
        Equipment equipment = equipmentRepository.findById(input.equipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        if (!isEquipmentAvailable(equipment, input.startDate(), input.endDate())) {
            throw new IllegalStateException("Equipment is not available for the selected dates");
        }

        User user = userRepository.findById(input.userId())
                .orElseThrow(()  -> new EntityNotFoundException("User not found"));
        if (!canUserRent(user)) {
            throw new IllegalStateException("User has reached the rental limit");
        }

        BigDecimal totalCost = equipment.calculateCost(Duration.between(input.startDate(), input.endDate()).toHours());

        Rental rental = Rental.builder()
                .status(RentalStatus.PENDING)
                .equipment(equipment)
                .user(user)
                .startDate(input.startDate())
                .endDate(input.endDate())
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

    public void payRental(PayRentalInput input) {
        Rental rental = rentalRepository.findById(input.rentalId())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        if (rental.getStatus() != RentalStatus.PENDING) {
            throw new PaymentStatusException();
        }

        try {
            PaymentDetails paymentDetails = new PaymentDetails(rental.getTotalCost(), rental.getUser().getEmail());
            paymentGatewayService.processPayment(paymentDetails);
        } catch (Exception e) {
            throw new PaymentProcessingException();
        }

        rental.setStatus(RentalStatus.ONGOING);
        rentalRepository.save(rental);
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
