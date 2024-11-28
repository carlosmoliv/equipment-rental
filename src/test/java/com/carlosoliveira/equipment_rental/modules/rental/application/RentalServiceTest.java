    package com.carlosoliveira.equipment_rental.modules.rental.application;

import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.repositories.EquipmentRepository;
import com.carlosoliveira.equipment_rental.modules.rental.domain.Rental;
import com.carlosoliveira.equipment_rental.modules.rental.domain.enums.RentalStatus;
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
            // Arrange
            equipment.setHourlyRate(BigDecimal.valueOf(10));
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusHours(50);
            CreateRentalInput input = new CreateRentalInput(user.getId(), equipment.getId(), startDate, endDate);

            when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

            // Act
            sut.create(input);

            // Assert
            verify(rentalRepository).save(argThat(rental ->
                    rental.getTotalCost().compareTo(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(50)).multiply(BigDecimal.valueOf(0.9))) == 0
            ));
        }

        @Test
        void rental_less_than_48_hours_does_not_receive_discount() {
            // Arrange
            equipment.setHourlyRate(BigDecimal.valueOf(10));
            LocalDateTime startDate = LocalDateTime.now();
            LocalDateTime endDate = startDate.plusHours(48);
            CreateRentalInput input = new CreateRentalInput(user.getId(), equipment.getId(), startDate, endDate);

            when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

            // Act
            sut.create(input);

            // Assert
            verify(rentalRepository).save(argThat(rental ->
                    rental.getTotalCost().compareTo(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(48))) == 0
            ));
        }

        @Test
        void equipment_not_available_cannot_be_rented() {
            CreateRentalInput input = new CreateRentalInput(user.getId(), equipment.getId(), startDate, endDate);
            lenient().when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
            lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            lenient().when(rentalRepository.findByEquipmentAndPeriod(equipment, startDate, endDate)).thenReturn(List.of(rental));

            IllegalStateException thrown =
                    assertThrows(IllegalStateException.class, () -> sut.create(input));
            assertThat(thrown.getMessage()).isEqualTo("Equipment is not available for the selected dates");
        }


        @Test
        void user_can_only_rent_up_to_2_items_at_a_time() {
            // Arrange
            CreateRentalInput input = new CreateRentalInput(user.getId(), equipment.getId(), startDate, endDate);

            when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            Rental ongoingRental1 = Rental.builder()
                    .status(RentalStatus.ONGOING)
                    .equipment(equipment)
                    .user(user)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            Rental ongoingRental2 = Rental.builder()
                    .status(RentalStatus.ONGOING)
                    .equipment(equipment)
                    .user(user)
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();

            when(rentalRepository.findByUserAndStatus(user, RentalStatus.ONGOING))
                    .thenReturn(List.of(ongoingRental1, ongoingRental2));

            // Act & Assert
            IllegalStateException thrown =
                    assertThrows(IllegalStateException.class, () -> sut.create(input));
            assertThat(thrown.getMessage()).isEqualTo("User has reached the rental limit");
        }
    }

    @Nested
    class ReturnTests {

        @Test
        void rental_is_successfully_returned() {
            rental.setStatus(RentalStatus.ONGOING);
            rental.setEndDate(LocalDateTime.now().minusHours(5));
            when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

            // Act
            Rental returnedRental = sut.returnRental(rental.getId());

            // Assert
            assertThat(returnedRental.getStatus()).isEqualTo(RentalStatus.COMPLETED);
            assertThat(returnedRental.getReturnDate()).isNotNull();
            assertThat(returnedRental.getLateFees()).isNotEqualTo(BigDecimal.ZERO);

            verify(rentalRepository).save(argThat(r ->
                    r.getStatus() == RentalStatus.COMPLETED && r.getReturnDate() != null
            ));
        }

        @Test
        void rental_returned_before_end_date_receives_no_late_fees() {
            // Arrange
            Rental rental = Rental.builder()
                    .status(RentalStatus.ONGOING)
                    .endDate(LocalDateTime.now().plusHours(2))
                    .equipment(Equipment.builder().lateFeeRate(BigDecimal.TEN).build())
                    .build();
            when(rentalRepository.findById(anyLong())).thenReturn(Optional.of(rental));

            // Act
            Rental returnedRental = sut.returnRental(1L);

            // Assert
            assertThat(returnedRental.getLateFees()).isEqualTo(BigDecimal.ZERO);
            verify(rentalRepository, times(2)).save(any(Rental.class));
        }

        @Test
        void rental_returned_after_end_date_receives_late_fees() {
            // Arrange
            Rental rental = Rental.builder()
                    .status(RentalStatus.ONGOING)
                    .endDate(LocalDateTime.now().minusHours(3))
                    .equipment(Equipment.builder().lateFeeRate(BigDecimal.TEN).build())
                    .build();

            when(rentalRepository.findById(anyLong())).thenReturn(Optional.of(rental));

            // Act
            Rental returnedRental = sut.returnRental(1L);

            // Assert
            assertThat(returnedRental.getLateFees()).isEqualTo(BigDecimal.valueOf(30));
            verify(rentalRepository, times(2)).save(any(Rental.class));
        }
    }
}
