package com.alkemy.ong.service;

import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.entity.Testimonial;
import com.alkemy.ong.mapper.TestimonialMapper;
import com.alkemy.ong.repository.TestimonialRepository;
import com.alkemy.ong.service.implement.TestimonialServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TestimonialServiceImpl.class})
@ExtendWith(SpringExtension.class)
class TestimonialServiceImplTest {

    @MockBean
    TestimonialRepository testimonialRepository;

    @SpyBean
    private TestimonialMapper testimonialMapper;

    private TestimonialServiceImpl testimonialServiceImpl;

    private Testimonial testimonial;

    @BeforeEach
    void setup(){
        testimonial = Testimonial.builder()
                .id("1")
                .name("name")
                .content("content")
                .build();

        testimonialServiceImpl = new TestimonialServiceImpl(testimonialMapper,testimonialRepository);
    }

    @Test
    void createTestimonial() {
        TestimonialDTO testimonialDTO = new TestimonialDTO();
        testimonialDTO.setName(testimonial.getName());
        testimonialDTO.setContent(testimonial.getContent());
        given(testimonialMapper.testimonialDTO2Entity(testimonialDTO)).willReturn(testimonial);
        given(testimonialMapper.testimonialEntity2DTO(testimonial)).willReturn(testimonialDTO);
        given(testimonialRepository.save(testimonial)).willReturn(testimonial);
        TestimonialDTO testimonialDTO1 = testimonialServiceImpl.createTestimonial(testimonialDTO);
        assertEquals(testimonialDTO1.getName(), testimonial.getName());
        verify(testimonialMapper,times(1)).testimonialDTO2Entity(testimonialDTO);
        verify(testimonialMapper,times(1)).testimonialEntity2DTO(testimonial);
    }

    @Test
    @DisplayName("Valid Case update")
    void updateTestimonial() throws NotFoundException {
        TestimonialDTO testimonialDTO = new TestimonialDTO();
        testimonialDTO.setName("name1");
        testimonialDTO.setContent("content1");

        given(testimonialRepository.save(any())).willReturn(testimonial);
        given(testimonialRepository.findById("1")).willReturn(Optional.of(testimonial));
        testimonialServiceImpl.updateTestimonial(testimonialDTO,"1");
        assertThat(testimonial.getName()).isEqualTo("name1");
        verify(testimonialRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Testimonial NotFound update")
    void updateTestimonialNotFound() {
        TestimonialDTO testimonialDTO = new TestimonialDTO();
        testimonialDTO.setName("name1");
        testimonialDTO.setContent("content1");
        given(testimonialRepository.findById("1")).willReturn(Optional.empty());
        assertThatThrownBy(()->testimonialServiceImpl.updateTestimonial(testimonialDTO,"1"))
                .isInstanceOf(NotFoundException.class);
        verify(testimonialRepository, never()).save(any());
    }

    @Test
    @DisplayName("Valid Case delete")
    void deleteTestimonial() throws NotFoundException {
        given(testimonialRepository.findById(testimonial.getId())).willReturn(Optional.of(testimonial));
        willDoNothing().given(testimonialRepository).deleteById(testimonial.getId());
        testimonialServiceImpl.deleteTestimonial(testimonial.getId());
        verify(testimonialRepository,times(1)).deleteById(testimonial.getId());
    }

    @Test
    @DisplayName("Testimonial NotFound delete")
    void deleteTestimonialNotFound() {
        given(testimonialRepository.findById(testimonial.getId())).willReturn(Optional.empty());
        assertThatThrownBy(()->testimonialServiceImpl.deleteTestimonial(testimonial.getId()))
                .isInstanceOf(NotFoundException.class);
        verify(testimonialRepository,never()).deleteById(testimonial.getId());
    }

    @Test
    @DisplayName("Valid Case get testimonial pages")
    void getAllForPages() {
        ArrayList<Testimonial> testimonials = new ArrayList<>();
        testimonials.add(new Testimonial());
        testimonials.add(new Testimonial());
        testimonials.add(new Testimonial());
        List<TestimonialDTO> testimonialDTOS = new ArrayList<>();

        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        Page<Testimonial> page = new PageImpl<>(testimonials,pageable,testimonials.size());

        when(testimonialRepository.findAll(any(Pageable.class))).thenReturn(page);
        PagesDTO<TestimonialDTO> testimonialPagesDTO = testimonialServiceImpl.getAllForPages(0);

        assertThat(testimonialPagesDTO).isNotNull();
        assertThat(testimonials.size()).isEqualTo(3);
        verify(testimonialMapper, times(1)).testimonialEntityPageDTOList(any());
    }

    @Test
    @DisplayName("Error get testimonials pages")
    void getAllForPagesError() {
        ArrayList<Testimonial> testimonials = new ArrayList<>();
        testimonials.add(new Testimonial());
        testimonials.add(new Testimonial());
        testimonials.add(new Testimonial());
        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        Page<Testimonial> page = new PageImpl<>(testimonials,pageable,testimonials.size());
        assertThatThrownBy(()->testimonialServiceImpl.getAllForPages(-1))
                .isInstanceOf(IllegalArgumentException.class);
        verify(testimonialMapper, never()).testimonialEntityPageDTOList(any());
    }
}