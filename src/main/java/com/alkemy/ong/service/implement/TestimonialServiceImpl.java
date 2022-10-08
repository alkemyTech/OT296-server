package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.service.TestimonialService;
import com.alkemy.ong.entity.Testimonial;
import com.alkemy.ong.mapper.TestimonialMapper;
import com.alkemy.ong.repository.TestimonialRepository;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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

	@Override
	public TestimonialDTO updateTestimonial(TestimonialDTO testimonialDTO, String id) throws NotFoundException {
		Testimonial testimonial= testimonialRepository.findById(id).orElse(null);
		if (testimonial == null){
			throw new NotFoundException("testimonial not found");
		}
		testimonial.setContent(testimonialDTO.getContent());
		testimonial.setImage(testimonialDTO.getImage());
		testimonial.setName(testimonialDTO.getName());
		Testimonial testimonialUpdated = testimonialRepository.save(testimonial);
		return testimonialMapper.testimonialEntity2DTO(testimonialUpdated);
	}

	public void deleteTestimonial(String id) throws NotFoundException{
		Testimonial testimonialEntity = testimonialRepository.findById(id).orElse(null);
		if (testimonialEntity == null) {
			throw new NotFoundException("Testimonial not found");
		}
		testimonialRepository.deleteById(id);
	}
	
	@Override
	public PagesDTO<TestimonialDTO> getAllForPages(int page) {
		if (page < 0) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}
		Pageable pageRequest = PageRequest.of(page, 10);
		Page<Testimonial> testimonial = testimonialRepository.findAll(pageRequest);
		return responsePage(testimonial);
	}

	private PagesDTO<TestimonialDTO> responsePage(Page<Testimonial> page) {
		Page<TestimonialDTO> response = new PageImpl<>(
				testimonialMapper.testimonialEntityPageDTOList(page.getContent()),
				PageRequest.of(page.getNumber(), page.getSize()),
				page.getTotalElements());
		return new PagesDTO<>(response, "localhost:8080/testimonials?page=");
	}
}