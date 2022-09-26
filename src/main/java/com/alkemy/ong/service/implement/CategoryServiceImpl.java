package com.alkemy.ong.service.implement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.entity.Category;
import com.alkemy.ong.mapper.CategoryMapper;
import com.alkemy.ong.repository.CategoryRepository;
import com.alkemy.ong.service.CategoryService;

import javassist.NotFoundException;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
    @PutMapping("/{id}")
    public void updateCategory(@RequestBody CategoryDTO dto, @PathVariable String id) throws NotFoundException{
    	Optional<Category> category = categoryRepository.findById(id);
    	if (category.isPresent()) {
    		 Category oldCategory = category.get();
    		 if (dto.getDescription() != null) {
    			 oldCategory.setDescription(dto.getDescription()); 
    		 }
    		 if (dto.getImage() != null) {
    			 oldCategory.setImage(dto.getImage()); 
    		 }
    		 if (dto.getName() != null) {
    			 oldCategory.setName(dto.getName()); 
    		 }
    		 if (dto.getTimestamps() != null) {
    			 oldCategory.setTimestamps(dto.getTimestamps()); 
    		 }
    		 categoryRepository.save(oldCategory);
    	} else {
    		throw new NotFoundException("Category not found");
    	}
	}
    
}
