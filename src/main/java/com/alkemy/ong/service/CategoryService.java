package com.alkemy.ong.service;

import com.alkemy.ong.dto.CategoryDTO;

import javassist.NotFoundException;

public interface CategoryService {

	public void updateCategory(CategoryDTO dto, String id) throws NotFoundException;

}
