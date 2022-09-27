package com.alkemy.ong.service;

import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.CategoryBasicDTO;
import java.util.List;

public interface CategoryService {

    CategoryDTO getCategoryById(String id);
    
    List<CategoryBasicDTO> getCategory();
}