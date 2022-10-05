package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.dto.CategoryDTO;
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

    public List<CategoryDTO> categoryEntityPageDTOList(List<Category> categories){
        List<CategoryDTO> categoryDTO = new ArrayList<>();
        for(Category category : categories){
            categoryDTO.add(this.categoryEntity2DTO(category));
        }
        return categoryDTO;
    }
}