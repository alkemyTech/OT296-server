package com.alkemy.ong.service;

import com.alkemy.ong.dto.CategoryDTO;

import javassist.NotFoundException;

import com.alkemy.ong.dto.CategoryBasicDTO;
import java.util.List;

public interface CategoryService {

    CategoryDTO getCategoryById(String id);

    List<CategoryBasicDTO> getCategory();

    void deleteCategory(String id) throws NotFoundException;

    CategoryDTO createCategory(CategoryDTO categoryDTO);
}