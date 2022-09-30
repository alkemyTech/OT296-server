package com.alkemy.ong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.service.TestimonialService;

@RestController
@RequestMapping("/testimonials")
public class TestimonialController {


	@Autowired
	private TestimonialService testimonialService;

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
}