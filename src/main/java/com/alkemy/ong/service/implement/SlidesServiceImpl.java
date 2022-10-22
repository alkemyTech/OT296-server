package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.entity.Slides;
import com.alkemy.ong.mapper.SlidesMapper;
import com.alkemy.ong.repository.OrganizationRepository;
import com.alkemy.ong.repository.SlidesRepository;
import com.alkemy.ong.service.SlidesService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
@AllArgsConstructor
@Service
public class SlidesServiceImpl implements SlidesService {
    @Autowired
    private SlidesRepository slidesRepository;
    @Autowired
    private SlidesMapper slidesMapper;

    @Autowired
    private OrganizationRepository organizationsRepository;

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

    @Override
    @Transactional
    public SlidesDTO createSlide(SlidesDTO dto) throws NotFoundException, IOException {
        Integer slideMaxOrder = slidesRepository.getMaxOrder();
        Slides entity = slidesMapper.SlideDTO2Entity(dto);
        Optional<Organization> organization = organizationsRepository.findById(entity.getOrganizationID());
        if (organization.isEmpty()) {
            throw new NotFoundException("Organization not found");
        }
        entity.setOrganization(organization.get());

        if (dto.getOrder() == null){
            entity.setOrder(slideMaxOrder + 1);
        } else if (dto.getOrder() != null) {
            entity.setOrder(dto.getOrder());
        } else if (Objects.equals(dto.getOrder(), slideMaxOrder)) {
            entity.setOrder(slideMaxOrder + 1);
        }
        slidesRepository.save(entity);
        return slidesMapper.SlidesEntity2DTO(entity);
    }


}
