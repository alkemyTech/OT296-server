package com.alkemy.ong.service;

import com.alkemy.ong.dto.OrganizationDTO;
import com.alkemy.ong.dto.OrganizationDTOPublic;
import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.entity.Slides;
import com.alkemy.ong.mapper.OrganizationMapper;
import com.alkemy.ong.repository.OrganizationRepository;
import com.alkemy.ong.repository.SlidesRepository;
import com.alkemy.ong.service.implement.OrganizationServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {OrganizationServiceImpl.class})
class OrganizationServiceImplTest {
    @MockBean
    OrganizationRepository organizationRepository;
    @MockBean
    SlidesRepository slidesRepository;
    @SpyBean
    OrganizationMapper organizationMapper;
    private OrganizationServiceImpl organizationServiceImpl;

    @BeforeEach
    void SetUp(){
        organizationServiceImpl = new OrganizationServiceImpl(organizationRepository, organizationMapper, slidesRepository);
    }
    static OrganizationDTO generateOrganizationDTO(){
        OrganizationDTO orgDTO = new OrganizationDTO();
        orgDTO.setName("name1");
        orgDTO.setImage("image1");
        orgDTO.setAddress("address1");
        orgDTO.setPhone(1234);
        orgDTO.setEmail("email1@email.com");
        orgDTO.setUrlFacebook("facebook1");
        orgDTO.setUrlLinkedin("linkedin1");
        orgDTO.setUrlInstagram("instagram1");
        orgDTO.setWelcomeText("welcome1");
        orgDTO.setAboutUsText("about1");
        return orgDTO;
    }
    static List<Organization> generateOrganizations() {
        Organization organization = new Organization();
        organization.setId("1");
        organization.setName("org1");
        organization.setImage("images1");
        organization.setAddress("address1");
        organization.setPhone(1234);
        organization.setEmail("email1@email.com");
        organization.setUrlFacebook("facebook1");
        organization.setUrlInstagram("instagram1");
        organization.setUrlLinkedin("linkedin1");
        organization.setWelcomeText("Welcome1");
        organization.setAboutUsText("About1");
        organization.setSlides(OrganizationServiceImplTest.generateSlides());
        return Collections.singletonList(organization);
    }
    static List<Slides> generateSlides() {
        Slides slide = new Slides();
        slide.setId("1");
        slide.setImageURL("image1");
        slide.setText("text1");
        slide.setOrder(1);
        slide.setOrganizationID("1");
        return Collections.singletonList(slide);
    }

    /*============================================================*/
    /*                           GET-TEST                         */
    /*============================================================*/

    @Test
    @DisplayName("Test Get Ong status .OK")
    void getOrganizationsDTOIsOk() {
        List<Organization> organizations = OrganizationServiceImplTest.generateOrganizations();
        given(organizationRepository.findAll()).willReturn(organizations);
        List<OrganizationDTOPublic> organizationDTOPublics = organizationMapper.organizationListEntity2DTO(organizations);

        given(organizationServiceImpl.getOrganizationsDTO()).willReturn(organizationDTOPublics);

        assertThat(organizationDTOPublics).isNotNull();
        assertThat(organizationDTOPublics.size()).isEqualTo(1);
        Mockito.verify(organizationRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Test GetByOngOrder Ong status .OK")
    void getSlidesByIdOngOrderIsOk() throws NotFoundException {
        Organization organization = OrganizationServiceImplTest.generateOrganizations().get(0);
        List<Slides> slides = slidesRepository.findSlidesByIdOrg(organization.getId());
        given(organizationRepository.findById(organization.getId())).willReturn(Optional.of(organization));
        given(slidesRepository.findSlidesByIdOrg(organization.getId())).willReturn(slides);

        organization.setSlides(slides);
        OrganizationDTOPublic organizationDTOPublic = organizationMapper.organizationEntity2DTO(organization);
        when(organizationServiceImpl.getSlidesByIdOngOrder(organization.getId())).thenReturn(organizationDTOPublic);

        assertThat(organizationDTOPublic).isNotNull();
        assertThat(organizationDTOPublic.getName()).isEqualTo("org1");
        assertThat(organization.getSlides()).isEqualTo(slides);
        Mockito.verify(organizationRepository).findById(Mockito.any());
    }

    @Test
    @DisplayName("Test Get Ong status .NOT_FOUND")
    void getSlidesByIdOngOrderIsNotFound() {
        Organization organization = OrganizationServiceImplTest.generateOrganizations().get(0);

        given(organizationRepository.findById(organization.getId())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> organizationServiceImpl.getSlidesByIdOngOrder(organization.getId()));
        Mockito.verify(organizationRepository, Mockito.times(1)).findById(Mockito.any());
    }

    /*============================================================*/
    /*                           POST-TEST                        */
    /*============================================================*/

    @Test
    @DisplayName("Test Post Ong status .OK")
    void createIsOk() {
        OrganizationDTO organizationDTO = OrganizationServiceImplTest.generateOrganizationDTO();
        Organization organization = organizationMapper.organizationDto2Entity(organizationDTO);

        given(organizationRepository.save(organization)).willReturn(organization);
        given(organizationMapper.organizationDto2Entity(organizationDTO)).willReturn(organization);
        given(organizationMapper.organizationEntity2DTOCreate(organization)).willReturn(organizationDTO);

        organizationServiceImpl.create(organizationDTO);

        assertThat(organization.getImage()).isEqualTo(organizationDTO.getImage());
        Mockito.verify(organizationRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test Post Ong status .INTERNAL_SERVER_ERROR")
    void createInternalError() {
        OrganizationDTO organizationDTO = OrganizationServiceImplTest.generateOrganizationDTO();
        Organization organization = organizationMapper.organizationDto2Entity(organizationDTO);

        given(organizationMapper.organizationDto2Entity(organizationDTO)).willReturn(organization);
        given(organizationRepository.save(organization)).willThrow(InternalError.class);

        assertThrows(InternalError.class, () -> organizationServiceImpl.create(organizationDTO));
        verify(organizationRepository, Mockito.times(1)).save(Mockito.any());
    }

    /*============================================================*/
    /*                           PATCH-TEST                       */
    /*============================================================*/

    @Test
    @DisplayName("Test Patch Ong status .OK")
    void patchOrganizationIsOk() throws NotFoundException {
        Organization organization = OrganizationServiceImplTest.generateOrganizations().get(0);
        given(organizationRepository.findById(Mockito.any())).willReturn(Optional.of(organization));

        Map<Object, Object> objectMap = new HashMap<>();
        objectMap.put("name", "nameUpdated");
        Field field = ReflectionUtils.findField(Organization.class, "name");
        field.setAccessible(true);
        ReflectionUtils.setField(field, organization, "nameUpdated");

        given(organizationRepository.save(organization)).willReturn(organization);
        OrganizationDTOPublic organizationDTOPublic = organizationMapper.organizationEntity2DTO(organization);

        when(organizationServiceImpl.patchOrganization(Mockito.any(),objectMap)).thenReturn(organizationDTOPublic);

        assertThat(organization.getName()).isEqualTo("nameUpdated");
        verify(organizationRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test Patch Ong status .NOT_FOUND")
    void patchOrganizationNotFound() {
        Organization organization = OrganizationServiceImplTest.generateOrganizations().get(0);
        given(organizationRepository.findById(Mockito.any())).willReturn(Optional.empty());

        Map<Object, Object> objectMap = new HashMap<>();

        assertThrows(NotFoundException.class, () -> organizationServiceImpl.patchOrganization(organization.getId(), objectMap));
        verify(organizationRepository, never()).save(Mockito.any());
    }

    @Test
    @DisplayName("Test Patch Ong status .INTERNAL_SERVER_ERROR")
    void patchOrganizationInternalError() {
        Organization organization = OrganizationServiceImplTest.generateOrganizations().get(0);
        given(organizationRepository.findById(Mockito.any())).willReturn(Optional.of(organization));

        Map<Object, Object> objectMap = new HashMap<>();
        given(organizationRepository.save(organization)).willThrow(InternalError.class);

        assertThrows(InternalError.class, () -> organizationServiceImpl.patchOrganization(organization.getId(), objectMap));
        verify(organizationRepository).findById(Mockito.any());
    }

}