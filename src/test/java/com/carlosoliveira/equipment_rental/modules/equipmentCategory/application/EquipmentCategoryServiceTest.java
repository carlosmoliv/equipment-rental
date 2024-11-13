package com.carlosoliveira.equipment_rental.modules.equipmentCategory.application;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.application.inputs.CreateEquipmentCategoryInput;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.domain.EquipmentCategory;
import com.carlosoliveira.equipment_rental.modules.equipmentCategory.infra.jpa.repositories.EquipmentCategoryRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentCategoryServiceTest {

    private CreateEquipmentCategoryInput createEquipmentCategoryInput;

    @Mock
    private EquipmentCategoryRepository categoryRepository;

    @InjectMocks
    private EquipmentCategoryService sut;

    @BeforeEach
    void setUp() {
        Faker faker = new Faker();
        createEquipmentCategoryInput = new CreateEquipmentCategoryInput(
                "Electronics"
        );
    }

    @Nested
    class CreateTests {

        @Test
        void creates_a_new_category() {
            sut.create(createEquipmentCategoryInput);

            verify(categoryRepository, times(1)).save(any(EquipmentCategory.class));
        }
    }

    @Nested
    class FindAllTests {

        @Test
        void list_all_categories() {
            // Arrange
            List<EquipmentCategory> mockCategories = List.of(
                    EquipmentCategory.builder().id(1L).name("Eletronics").build(),
                    EquipmentCategory.builder().id(2L).name("Furniture").build()
            );

            when(categoryRepository.findAll()).thenReturn(mockCategories);

            // Act
            List<EquipmentCategory> categories = sut.findAll();

            // Assert
            assertThat(categories, hasSize(2));
            assertThat(categories, is(mockCategories));
        }
    }
}
