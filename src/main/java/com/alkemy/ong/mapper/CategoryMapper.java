package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category categoryDTO2Entity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setImage(categoryDTO.getImage());
        category.setTimestamps(categoryDTO.getTimestamps());
        return category;
    }

    public CategoryDTO categoryEntity2DTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setImage(category.getImage());
        categoryDTO.setTimestamps(category.getTimestamps());
        return categoryDTO;
    }
}
