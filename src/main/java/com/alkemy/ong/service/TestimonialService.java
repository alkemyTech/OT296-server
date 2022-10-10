package com.alkemy.ong.service;

import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.dto.TestimonialDTO;

import javassist.NotFoundException;

public interface TestimonialService {

	TestimonialDTO createTestimonial(TestimonialDTO testimonial);
	
	public PagesDTO<TestimonialDTO> getAllForPages(int page);
	
	TestimonialDTO updateTestimonial(TestimonialDTO dto, String id) throws NotFoundException;

	public void deleteTestimonial(String id) throws NotFoundException;
}