package com.alkemy.ong.service;

import com.alkemy.ong.dto.CategoryDTO;

import com.alkemy.ong.dto.PagesDTO;
import javassist.NotFoundException;

import com.alkemy.ong.dto.CategoryBasicDTO;

import java.util.List;

public interface CategoryService {

    public PagesDTO<CategoryDTO> getAllForPages(int page);

    CategoryDTO getCategoryById(String id);

    List<CategoryBasicDTO> getCategory();
    
    CategoryDTO updateCategory(CategoryDTO dto, String id) throws NotFoundException;

    void deleteCategory(String id) throws NotFoundException;

    CategoryDTO createCategory(CategoryDTO categoryDTO);
}
