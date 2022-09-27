package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.entity.Category;
import com.alkemy.ong.mapper.CategoryMapper;
import com.alkemy.ong.repository.CategoryRepository;
import com.alkemy.ong.service.CategoryService;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    
    @PutMapping("/{id}")
    CategoryDTO updateCategory(@RequestBody CategoryDTO dto, @PathVariable String id) throws NotFoundException{
    	Optional<Category> category = categoryRepository.findById(id);
    	Category categoryUpdated = categoryMapper.categoryDTO2Entity(dto);
    	Category oldCategory = new Category();
    	if (category.isPresent()) {
    		 oldCategory = category.get();
    		 if (categoryUpdated.getDescription() != null) {
    			 oldCategory.setDescription(dto.getDescription()); 
    		 }
    		 if (categoryUpdated.getImage() != null) {
    			 oldCategory.setImage(dto.getImage()); 
    		 }
    		 if (categoryUpdated.getName() != null) {
    			 oldCategory.setName(dto.getName()); 
    		 }
    		 if (categoryUpdated.getTimestamps() != null) {
    			 oldCategory.setTimestamps(dto.getTimestamps()); 
    		 }
    		 categoryRepository.save(oldCategory);
    	} else {
    		throw new NotFoundException("Category not found");
    	}
    	return categoryMapper.categoryEntity2DTO(oldCategory);
	}
}
