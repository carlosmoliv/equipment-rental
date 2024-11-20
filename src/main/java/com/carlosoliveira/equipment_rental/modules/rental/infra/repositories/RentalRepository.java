package com.carlosoliveira.equipment_rental.modules.rental.infra.repositories;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.domain.enums.RentalStatus;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByEquipmentAndPeriod(Equipment equipment, LocalDateTime startDate, LocalDateTime endDate);
    List<Rental> findByUserAndStatus(User user, RentalStatus rentalStatus);
}
