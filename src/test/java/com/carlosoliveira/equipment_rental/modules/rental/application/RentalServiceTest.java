package com.carlosoliveira.equipment_rental.modules.rental.application;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.repositories.EquipmentRepository;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.infra.repositories.RentalRepository;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;
import com.carlosoliveira.equipment_rental.modules.user.infra.jpa.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    EquipmentRepository equipmentRepository;

    @Mock
    RentalRepository rentalRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    RentalService sut;

    Faker faker = new Faker();
    private Equipment equipment;
    private User user;
    private Rental rental;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setup() {
        equipment = Equipment.builder().id(1L).name("Drill").hourlyRate(BigDecimal.valueOf(5)).build();
        user = User.builder().id(1L).firstName(faker.name().firstName()).lastName(faker.name().lastName()).email(faker.internet().emailAddress()).build();
        startDate = LocalDateTime.now().plusDays(1);
        endDate = startDate.plusHours(5);
        rental = Rental.builder().id(1L).user(user).equipment(equipment).startDate(startDate).endDate(endDate).build();
    }

    @Nested
    class CreateTests {

        @Test
        void rental_is_created_when_valid_data_is_provided() {
            CreateRentalInput input = new CreateRentalInput(user.getId(), equipment.getId(), startDate, endDate);
            when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

            sut.create(input);

            verify(rentalRepository).save(argThat(rental ->
                    rental.getUser().equals(user) &&
                            rental.getEquipment().equals(equipment) &&
                            rental.getStartDate().equals(startDate) &&
                            rental.getEndDate().equals(endDate) &&
                            rental.getTotalCost().equals(equipment.getHourlyRate()
                                    .multiply(BigDecimal.valueOf(Duration.between(startDate, endDate).toHours())))
            ));
        }
    }
}
