package com.alkemy.ong.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.entity.Testimonial;
import com.alkemy.ong.mapper.TestimonialMapper;
import com.alkemy.ong.repository.TestimonialRepository;
import com.alkemy.ong.service.TestimonialService;

@Service
public class TestimonialServiceImpl implements TestimonialService {

	@Autowired
	private TestimonialMapper testimonialMapper;

	@Autowired
	private TestimonialRepository testimonialRepository;

	@Override
	public TestimonialDTO createTestimonial(TestimonialDTO testimonialDTO) {
		Testimonial testimonial = testimonialMapper.testimonialDTO2Entity(testimonialDTO);
		Testimonial testimonialSave = testimonialRepository.save(testimonial);
		return testimonialMapper.testimonialEntity2DTO(testimonialSave);
	}
}