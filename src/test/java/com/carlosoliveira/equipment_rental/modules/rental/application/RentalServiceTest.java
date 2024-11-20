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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

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

        @Test
        void rental_over_48_hours_receives_10_percent_discount() {
            equipment.setHourlyRate(BigDecimal.valueOf(10));
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusHours(50);

            BigDecimal totalCost = sut.calculateTotalCost(equipment, startDate, endDate);

            BigDecimal expectedCost = BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(50))
                    .multiply(BigDecimal.valueOf(0.9));
            assertThat(totalCost).isEqualByComparingTo(expectedCost);
        }

        @Test
        void rental_less_than_48_hours_do_not_receives_10_percent_discount() {
            equipment.setHourlyRate(BigDecimal.valueOf(10));
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusHours(48);


            BigDecimal totalCost = sut.calculateTotalCost(equipment, startDate, endDate);

            BigDecimal expectedCost = BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(48));
            assertThat(totalCost).isEqualByComparingTo(expectedCost);
        }

        @Test
        void shouldThrowExceptionWhenEquipmentIsNotAvailable() {
            lenient().when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
            lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            lenient().when(rentalRepository.findByEquipmentAndPeriod(equipment, startDate, endDate)).thenReturn(List.of(rental));
            CreateRentalInput input = new CreateRentalInput(user.getId(), equipment.getId(), startDate, endDate);

            IllegalStateException thrown =
                    assertThrows(IllegalStateException.class, () -> {
                        sut.create(input);
                    });
            assertThat(thrown.getMessage()).isEqualTo("Equipment is not available for the selected dates");
        }
    }
}
