package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.dto.OrganizationDTOPublic;
import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.entity.Slides;
import com.alkemy.ong.mapper.OrganizationMapper;
import com.alkemy.ong.repository.OrganizationRepository;
import com.alkemy.ong.repository.SlidesRepository;
import com.alkemy.ong.service.OrganizationService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private SlidesRepository slidesRepository;


    @Override
    public List<OrganizationDTOPublic> getOrganizationsDTO() {
        List<Organization> organizations = organizationRepository.findAll();
        return organizationMapper.organizationListEntity2DTO(organizations);
    }
    @Override
    public OrganizationDTO create(OrganizationDTO dto) {
        Organization organization=organizationMapper.organizationDto2Entity(dto);
        Organization organizationCreated=organizationRepository.save(organization);
        OrganizationDTO result=organizationMapper.organizationEntity2DTOCreate(organizationCreated);
        System.out.println("Organization created");
        return result;
    }
    @Override
    public OrganizationDTOPublic patchOrganization(String id, Map<Object, Object> objectMap) throws NotFoundException {
        Optional<Organization> organization = organizationRepository.findById(id);
        if(organization.isPresent()){
            Organization replace = organization.get();
            objectMap.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Organization.class, (String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, replace, value);
            });
            organizationRepository.save(replace);
            return organizationMapper.organizationEntity2DTO(replace);
        }else {
            throw new NotFoundException("Organization not found");
        }
    }

    @Override
    public OrganizationDTOPublic getSlidesByIdOngOrder(String idOng) throws NotFoundException {
        Organization organization = organizationRepository.findById(idOng).orElse(null);
        if(organization == null){
            throw new NotFoundException("Organization not found");
        }
        List<Slides> slides = slidesRepository.findSlidesByIdOrg(idOng);
        organization.setSlides(slides);
        return organizationMapper.organizationEntity2DTO(organization);
    }
}
