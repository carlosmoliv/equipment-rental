package com.carlosoliveira.equipment_rental.helpers;

import com.carlosoliveira.equipment_rental.modules.equipmentCategory.presenters.dtos.CreateEquipmentCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryTestHelper {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthTestHelper authTestHelper;

    public Long getCategoryId() {
        CreateEquipmentCategoryDto createCategoryDto = new CreateEquipmentCategoryDto("Electronics");
        HttpEntity<CreateEquipmentCategoryDto> categoryRequest = new HttpEntity<>(createCategoryDto, authTestHelper.getHeadersWithAuth());
        ResponseEntity<String> categoryResponse = restTemplate.postForEntity(
                "/api/categories",
                categoryRequest,
                String.class
        );
        String categoryId = categoryResponse.getBody();
        assert categoryId != null;
        return Long.valueOf(categoryId);
    }
}
