package com.carlosoliveira.equipment_rental.modules.rental.repositories;

import com.carlosoliveira.equipment_rental.modules.equipment.entities.Equipment;
import com.carlosoliveira.equipment_rental.modules.rental.entities.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.enums.RentalStatus;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r WHERE r.equipment = :equipment " +
            "AND r.startDate <= :endDate " +
            "AND r.endDate >= :startDate")
    List<Rental> findByEquipmentAndPeriod(Equipment equipment, LocalDateTime startDate, LocalDateTime endDate);
    List<Rental> findByUserAndStatus(User user, RentalStatus rentalStatus);
    List<Rental> findByUser(User user);
    List<Rental> findByEquipment(Equipment equipment);
}
