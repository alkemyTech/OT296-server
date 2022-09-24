package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.entity.Category;
import com.alkemy.ong.mapper.CategoryMapper;
import com.alkemy.ong.repository.CategoryRepository;
import com.alkemy.ong.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public CategoryDTO getCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        assert category != null;
        return categoryMapper.categoryEntity2DTO(category);
    }
}
