package com.alkemy.ong.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.service.TestimonialService;

@RestController
@RequestMapping("/testimonials")
public class TestimonialController {

	@Autowired
	private TestimonialService testimonialService;

	@GetMapping(params = "page")
	public ResponseEntity<?> getPageTestimonial(@RequestParam(defaultValue = "0") int page) {
		PagesDTO<TestimonialDTO> response = testimonialService.getAllForPages(page);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping
	public ResponseEntity<TestimonialDTO> createTestimonial(@Valid @RequestBody TestimonialDTO testimonial) {
		testimonialService.createTestimonial(testimonial);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<TestimonialDTO> update(@PathVariable String id, @RequestBody TestimonialDTO dto) {
		try{
			TestimonialDTO testimonialDTO = testimonialService.updateTestimonial(dto, id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(testimonialDTO);
		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteTestimonial (@PathVariable String id){
		try{
			testimonialService.deleteTestimonial(id);
		}
		catch (NotFoundException e){
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}