package com.alkemy.ong.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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

	@PutMapping("{/id}")
	public ResponseEntity<TestimonialDTO> update(@PathVariable String id, @RequestBody TestimonialDTO dto) {
		try{
			TestimonialDTO testimonialDTO = testimonialService.updateTestimonial(dto, id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(testimonialDTO);
		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

	}
}