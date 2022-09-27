package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryBasicDTO>> getCategory(){
        List<CategoryBasicDTO> categoryBasicDTOS = categoryService.getCategory();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryBasicDTOS);
    }
    
	@PutMapping("/{id}")
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO category, @PathVariable String id) {
		try {
			categoryService.updateCategory(category, id);
		} catch (NotFoundException e) {
			return new ResponseEntity<CategoryDTO>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
