package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.entity.Slides;
import com.alkemy.ong.mapper.SlidesMapper;
import com.alkemy.ong.repository.SlidesRepository;
import com.alkemy.ong.service.SlidesService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SlidesServiceImpl implements SlidesService {
    @Autowired
    private SlidesRepository slidesRepository;
    @Autowired
    private SlidesMapper slidesMapper;
    @Override
    public List<SlidesDTOPublic> getSlidesDTO() {
        List<Slides> entities = slidesRepository.findAll();
        return slidesMapper.slidesEntityList2DTO(entities);
    }

    @Override
    public SlidesDTO getSlideDTO(String id) throws NotFoundException {
        if (!slidesRepository.existsById(id)) {
            throw new NotFoundException("Slide not found");
        }
        Slides entity = slidesRepository.findById(id).orElse(null);
        assert entity != null;
        return slidesMapper.SlidesEntity2DTO(entity);
    }

    @Override
    public SlidesDTO updateSlide(String id, SlidesDTO slideDTO) throws NotFoundException {
        Slides slides = slidesRepository.findById(id).orElse(null);
        if(slides == null){
            throw new NotFoundException("Slide not found");
        }
        slides.setImageURL(slideDTO.getImageURL());
        slides.setText(slideDTO.getText());
        slides.setOrder(slideDTO.getOrder());
        slides.setOrganizationID(slideDTO.getOrganizationID());
        Slides slidesSave = slidesRepository.save(slides);
        return slidesMapper.SlidesEntity2DTO(slidesSave);
    }

    @Override
    public void deleteSlide(String id) throws NotFoundException {
        Slides slides = slidesRepository.findById(id).orElse(null);
        if(slides == null) {
            throw new NotFoundException("Slide not found");
        }
        slidesRepository.deleteById(id);
    }
}
