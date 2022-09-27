package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.entity.Category;
import com.alkemy.ong.mapper.CategoryMapper;
import com.alkemy.ong.repository.CategoryRepository;
import com.alkemy.ong.service.CategoryService;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<CategoryBasicDTO> getCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryBasicDTO> categoryBasicDTOS = categoryMapper.categoryEntityList2DTO(categories);
        return categoryBasicDTOS;
    }

    @Override
    public CategoryDTO getCategoryById(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        assert category != null;
        return categoryMapper.categoryEntity2DTO(category);
    }

	@Override
	public void deleteCategory(String id) throws NotFoundException {
    	Optional<Category> category = categoryRepository.findById(id);
    	if (category.isPresent()) {
    		categoryRepository.deleteById(id);
    	} else {
    		throw new NotFoundException("Category not found");
    	}
	}

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.categoryDTO2Entity(categoryDTO);
        Category categorySave = categoryRepository.save(category);
        return categoryMapper.categoryEntity2DTO(categorySave);
    }
}