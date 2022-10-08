package com.alkemy.ong.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.entity.Testimonial;

@Component
public class TestimonialMapper {

	public Testimonial testimonialDTO2Entity (TestimonialDTO testimonialDTO) {
		Testimonial testimonial = new Testimonial();
		testimonial.setContent(testimonialDTO.getContent());
		testimonial.setImage(testimonialDTO.getImage());
		testimonial.setName(testimonialDTO.getName());
		return testimonial;
	}

	public TestimonialDTO testimonialEntity2DTO (Testimonial testimonial) {
		TestimonialDTO testimonialDTO = new TestimonialDTO();
		testimonialDTO.setContent(testimonial.getContent());
		testimonialDTO.setImage(testimonial.getImage());
		testimonialDTO.setName(testimonial.getName());
		return testimonialDTO; 
	}
	
	public List<TestimonialDTO> testimonialEntityPageDTOList(List<Testimonial> testimonials){
		List<TestimonialDTO> testimonialDTO = new ArrayList<>();
		for(Testimonial testimonial: testimonials){
			testimonialDTO.add(this.testimonialEntity2DTO(testimonial));
		}
		return testimonialDTO;
	}
}