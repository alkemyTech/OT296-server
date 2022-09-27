package com.alkemy.ong.service;

import com.alkemy.ong.dto.CategoryBasicDTO;

import javassist.NotFoundException;

import java.util.List;

public interface CategoryService {

    List<CategoryBasicDTO> getCategory();
    
    CategoryDTO updateCategory(CategoryDTO dto, String id) throws NotFoundException;
}
