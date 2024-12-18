package com.carlosoliveira.equipment_rental.helpers;

import com.carlosoliveira.equipment_rental.modules.equipment.presenters.dtos.CreateEquipmentDto;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EquipmentTestHelper {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestHelper authTestHelper;

    @Autowired
    private CategoryTestHelper categoryTestHelper;
    
    public Long createEquipment() {
        Faker faker = new Faker();
        CreateEquipmentDto createEquipmentDto = new CreateEquipmentDto(
                faker.commerce().productName(),
                faker.lorem().paragraph(3),
                BigDecimal.valueOf(faker.number().randomDouble(2, 50, 1000)),
                true,
                categoryTestHelper.getCategoryId(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 50, 1000)),
                BigDecimal.valueOf(faker.number().randomDouble(2, 1, 20))
        );
        HttpEntity<CreateEquipmentDto> equipmentDto = new HttpEntity<>(createEquipmentDto, authTestHelper.getHeadersWithAuth());
        ResponseEntity<Long> response = restTemplate.postForEntity("/api/equipments", equipmentDto, Long.class);
        return response.getBody();
    }
}
