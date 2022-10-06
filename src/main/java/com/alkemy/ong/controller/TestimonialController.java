package com.alkemy.ong.controller;

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

import javax.validation.Valid;

import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.service.TestimonialService;

@RestController
@RequestMapping("/testimonials")
@Tag(name = "Testimonial Controller", description = "Create, delete and update Testimonial")
public class TestimonialController {


	@Autowired
	private TestimonialService testimonialService;

	@Operation(summary = "POST Testimonial")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Return created status code",
					content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad request",
					content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
					content = @Content)
	})
	@PostMapping
	public ResponseEntity<TestimonialDTO> createTestimonial(@Valid @RequestBody TestimonialDTO testimonial) {
		testimonialService.createTestimonial(testimonial);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "UPDATE testimonial")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Update testimonial",
					content = {@Content(mediaType = "application/json",
					schema = @Schema(implementation = TestimonialDTO.class))}),
			@ApiResponse(responseCode = "400", description = "Bad request",
					content = @Content),
			@ApiResponse(responseCode = "404", description = "Testimonial not found",
					content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
					content = @Content)
	})
	@PutMapping("/{id}")
	public ResponseEntity<TestimonialDTO> update(@Parameter(description = "Id of Testimonial to update", required = true) @PathVariable String id,
												 @RequestBody TestimonialDTO dto) {
		try{
			TestimonialDTO testimonialDTO = testimonialService.updateTestimonial(dto, id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(testimonialDTO);
		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@Operation(summary = "DELETE testimonial")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Testimonial deleted",
					content = @Content),
			@ApiResponse(responseCode = "400", description = "Bad request",
					content = @Content),
			@ApiResponse(responseCode = "404", description = "Testimonial not found",
					content = @Content),
			@ApiResponse(responseCode = "403", description = "Forbidden for no authenticated user",
					content = @Content)
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteTestimonial (@Parameter(description = "Id of Testimonial to delete", required = true) @PathVariable String id){
		try{
			testimonialService.deleteTestimonial(id);
		}
		catch (NotFoundException e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}