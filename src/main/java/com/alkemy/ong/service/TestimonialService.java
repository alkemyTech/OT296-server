package com.alkemy.ong.service;

import com.alkemy.ong.dto.TestimonialDTO;

import javassist.NotFoundException;

public interface TestimonialService {

	TestimonialDTO createTestimonial(TestimonialDTO testimonial);
	TestimonialDTO updateTestimonial(TestimonialDTO dto, String id) throws NotFoundException;
}