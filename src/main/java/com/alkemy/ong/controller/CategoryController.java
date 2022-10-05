package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.service.CategoryService;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(params = "page")
    public ResponseEntity<?> getPageCategory(@RequestParam(defaultValue = "0") int page) {
        PagesDTO<CategoryDTO> response = categoryService.getAllForPages(page);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String id) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            return ResponseEntity.status(HttpStatus.FOUND).body(categoryDTO);
        } catch (Exception e) {
            return new ResponseEntity("Organization not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping()
    public ResponseEntity<List<CategoryBasicDTO>> getCategory () {
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

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
		try {
			categoryService.deleteCategory(id);
		} catch (NotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

    @PostMapping
    public ResponseEntity<CategoryDTO> Create (@Valid @RequestBody CategoryDTO category) {
        categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
