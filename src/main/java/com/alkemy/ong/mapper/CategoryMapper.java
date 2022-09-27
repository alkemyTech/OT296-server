package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.entity.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {


    public CategoryBasicDTO categoryEntity2DTOBasic(Category category) {
        CategoryBasicDTO categoryBasicDTO = new CategoryBasicDTO();
        categoryBasicDTO.setName(category.getName());
        return categoryBasicDTO;
    }

    public List<CategoryBasicDTO> categoryEntityList2DTO(List<Category> categories) {
        List<CategoryBasicDTO> categoryBasicDTOS = new ArrayList<>();
        for (Category category : categories) {
            categoryBasicDTOS.add(this.categoryEntity2DTOBasic(category));
        }
        return categoryBasicDTOS;
    }
}
