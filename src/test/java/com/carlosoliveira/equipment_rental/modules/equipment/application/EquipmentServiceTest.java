package com.carlosoliveira.equipment_rental.modules.equipment.application;

import com.carlosoliveira.equipment_rental.modules.equipment.application.inputs.CreateEquipmentInput;
import com.carlosoliveira.equipment_rental.modules.equipment.infra.jpa.repositories.EquipmentRepository;
import com.carlosoliveira.equipment_rental.modules.equipment.domain.Equipment;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.infra.jpa.repositories.EquipmentCategoryRepository;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import com.github.javafaker.Faker;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    private CreateEquipmentInput createEquipmentInput;

    @Mock
    private EquipmentCategory equipmentCategory;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentCategoryRepository categoryRepository;

    @InjectMocks
    private EquipmentService sut;

    @BeforeEach
    void setUp() {
        Faker faker = new Faker();
        createEquipmentInput = new CreateEquipmentInput(
            faker.commerce().productName(),
            faker.lorem().sentence(),
            BigDecimal.valueOf(faker.number().randomDouble(2, 50, 1000)),
            true,
            123L
        );
        equipmentCategory = EquipmentCategory.builder()
                .id(123L)
                .name("Electronics")
                .equipments(null)
                .build();
    }

    @Nested
    class Create {

        @Test
        void creates_a_new_equipment() {
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(equipmentCategory));

            sut.create(createEquipmentInput);

            verify(equipmentRepository, times(1)).save(any(Equipment.class));
        }

        @Test
        void throws_exception_when_category_not_found() {
            // Arrange
            when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> sut.create(createEquipmentInput));

            // Verify that save is never called
            verify(equipmentRepository, never()).save(any(Equipment.class));
        }
    }
}