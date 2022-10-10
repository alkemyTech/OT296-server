package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "Category")
    @Operation(summary = "Get page of category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the categories",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "403", description = "User unauthorized",
                    content = @Content),
    })
    @GetMapping(params = "page")
    public ResponseEntity<?> getPageCategory(@RequestParam(defaultValue = "0") int page) {
        PagesDTO<CategoryDTO> response = categoryService.getAllForPages(page);
        return ResponseEntity.ok().body(response);
    }

    @Tag(name = "Category")
    @Operation(summary = "Get a category by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "403", description = "User unauthorized",
            content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@Parameter(description = "id of category to be searched")
                                                           @PathVariable String id) {
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
        } catch (Exception e) {
            return new ResponseEntity("Category not found", HttpStatus.NOT_FOUND);
        }
    }

    @Tag(name = "Category")
    @GetMapping()
    public ResponseEntity<List<CategoryBasicDTO>> getCategory () {
        List<CategoryBasicDTO> categoryBasicDTOS = categoryService.getCategory();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(categoryBasicDTOS);
    }

    @Tag(name = "Category")
    @Operation(summary = "Edit a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edited category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Certain arguments are required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO category, @Parameter(description = "id of category to be searched") @PathVariable String id) {
      try {
        categoryService.updateCategory(category, id);
      } catch (NotFoundException e) {
        return new ResponseEntity<CategoryDTO>(HttpStatus.NOT_FOUND);
      }
      return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Tag(name = "Category")
    @Operation(summary = "Delete a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted category",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User unauthorized",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content)
    })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@Parameter(description = "id of category to be searched") @PathVariable String id) {
		try {
			categoryService.deleteCategory(id);
		} catch (NotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

    @Tag(name = "Category")
    @Operation(summary = "Create a category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created category",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Certain arguments are required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "User unauthorized",
                    content = @Content),
    })
    @PostMapping
    public ResponseEntity<CategoryDTO> Create (@Valid @RequestBody CategoryDTO category) {
        categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
