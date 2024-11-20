package com.carlosoliveira.equipment_rental.modules.rental.application;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.repositories.EquipmentRepository;
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

    public void create(CreateRentalInput input) {
        Equipment equipment = equipmentRepository.findById(input.equipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));
        if (!isEquipmentAvailable(equipment, input.startDate(), input.endDate())) {
            throw new IllegalStateException("Equipment is not available for the selected dates");
        }

        User user = userRepository.findById(input.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        BigDecimal totalCost = calculateTotalCost(equipment, input.startDate(), input.endDate());

        Rental rental = Rental.builder()
                .status(RentalStatus.PENDING)
                .equipment(equipment)
                .user(user)
                .startDate(input.startDate())
                .endDate(input.endDate())
                .totalCost(totalCost)
                .build();
        rentalRepository.save(rental);
    }

    private boolean isEquipmentAvailable(Equipment equipment, LocalDateTime startDate, LocalDateTime endDate) {
        List<Rental> rentals = rentalRepository.findByEquipmentAndPeriod(equipment, startDate, endDate);
        return rentals.isEmpty();
    }

    public BigDecimal calculateTotalCost(Equipment equipment, LocalDateTime startDate, LocalDateTime endDate) {
        long durationInHours = Duration.between(startDate, endDate).toHours();
        BigDecimal rate = equipment.getHourlyRate();
        BigDecimal totalCost = rate.multiply(BigDecimal.valueOf(durationInHours));
        if (durationInHours > 48) {
            totalCost = totalCost.multiply(BigDecimal.valueOf(0.9));
        }
        return totalCost;
    }
}
