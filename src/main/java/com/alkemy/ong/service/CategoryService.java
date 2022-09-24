package com.alkemy.ong.service;

import com.alkemy.ong.dto.CategoryDTO;

public interface CategoryService {

    CategoryDTO getCategoryById (String id) throws Exception;
}
