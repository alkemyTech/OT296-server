package com.alkemy.ong.service;

import com.alkemy.ong.awsS3.service.AmazonClient;
import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.entity.Organization;
import com.alkemy.ong.entity.Slides;
import com.alkemy.ong.mapper.SlidesMapper;
import com.alkemy.ong.repository.OrganizationRepository;
import com.alkemy.ong.repository.SlidesRepository;
import com.alkemy.ong.service.implement.SlidesServiceImpl;
import com.alkemy.ong.utils.ImageHelper;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SlidesServiceImpl.class})
public class SlidesServiceImplTest {
    @MockBean
    SlidesRepository slidesRepository;
    @MockBean
    OrganizationRepository organizationRepository;
    @MockBean
    private ImageHelper imageHelper;
    @MockBean
    private AmazonClient amazonClient;
    @SpyBean
    SlidesMapper slidesMapper;
    @InjectMocks
    private SlidesServiceImpl slidesServiceImpl;

    @BeforeEach
    void SetUp(){
        slidesServiceImpl = new SlidesServiceImpl(slidesRepository,slidesMapper,organizationRepository);
    }

    static SlidesDTO generateSlidesDto(){
        SlidesDTO slidesDTO= new SlidesDTO();
        slidesDTO.setImageURL("image1");
        slidesDTO.setText("text1");
        slidesDTO.setOrder(0);
        slidesDTO.setOrganizationID(generateOrganizations().getId());
        return slidesDTO;
    }
    static SlidesDTOPublic generateSlidesDtoPublic(){
        SlidesDTOPublic slidesDTOPublic= new SlidesDTOPublic();
        slidesDTOPublic.setImageURL("image2");
        slidesDTOPublic.setOrder(1);
        return slidesDTOPublic;
    }
    static List<Slides> generateSlidesLis() {
        Slides slideLis = new Slides();
        slideLis.setId("1");
        slideLis.setImageURL("image2");
        slideLis.setOrder(1);
        return Collections.singletonList(slideLis);
    }
    static Organization generateOrganizations() {
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
        organization.setSlides(SlidesServiceImplTest.generateSlidesLis());
        return (organization);
    }
    @Nested
    class getSlides{
        @Test
        @DisplayName("Test Get All Slides status .OK")
        void getSlidesDTO_Ok() {
            Slides slides1= SlidesServiceImplTest.generateSlidesLis().get(0);
            given(slidesRepository.save(slides1)).willReturn(slides1);
            List<Slides> slides = SlidesServiceImplTest.generateSlidesLis();
            given(slidesRepository.findAll()).willReturn(slides);
            List<SlidesDTOPublic> slidesDTOPublic = slidesMapper.slidesEntityList2DTO(slides);
            given(slidesServiceImpl.getSlidesDTO()).willReturn(slidesDTOPublic);
            assertThat(slidesDTOPublic).isNotNull();
            assertThat(slidesDTOPublic.size()).isEqualTo(1);
            Mockito.verify(slidesRepository, Mockito.times(1)).findAll();
        }
    }
    @Nested
    class getSlidesById{
        @Test
        @DisplayName("Test Get Id Slides status ok ById")
        void getSlideDTO_IdOk() throws NotFoundException {
            Slides slides= SlidesServiceImplTest.generateSlidesLis().get(0);
            SlidesDTO slidesDTO= slidesMapper.SlidesEntity2DTO(slides);
            given(slidesRepository.findById(slides.getId())).willReturn(Optional.of(slides));
            given(slidesRepository.existsById("1")).willReturn(true);
            given(slidesServiceImpl.getSlideDTO("1")).willReturn(slidesDTO);
            assertThat(slides.getId()).isNotEmpty();
            verify(slidesRepository, Mockito.times(1)).findById(Mockito.any());
        }
        @Test
        @DisplayName("Test Get Id Slides status Not Found")
        void getSlideDTO_404(){
            Slides slides= SlidesServiceImplTest.generateSlidesLis().get(0);
            given(slidesRepository.findById(slides.getId())).willReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> slidesServiceImpl.getSlideDTO(slides.getId()));
            verify(slidesRepository, never()).findById(Mockito.any());
        }

    }
    @Nested
    class updateSlides{
        @Test
        @DisplayName("Test Update Id Slides status ok")
        void updateSlide() throws NotFoundException {
            Slides slides= SlidesServiceImplTest.generateSlidesLis().get(0);
            SlidesDTO slidesDTO= slidesMapper.SlidesEntity2DTO(slides);
            given(slidesRepository.save(slides)).willReturn(slides);
            given(slidesRepository.findById(slides.getId())).willReturn(Optional.of(slides));
            given(slidesServiceImpl.updateSlide("1",slidesDTO)).willReturn(slidesDTO);
            assertThat(slides.getId()).isNotEmpty();
            assertThat(slides.getImageURL()).isEqualTo("image2");
            verify(slidesRepository, Mockito.times(1)).save(Mockito.any());
        }
        @Test
        @DisplayName("Test Update Id Slides status Not Found")
        void updateSlide_404(){
            Slides slides= SlidesServiceImplTest.generateSlidesLis().get(0);
            SlidesDTO slidesDTO= slidesMapper.SlidesEntity2DTO(slides);
            given(slidesRepository.findById(slides.getId())).willReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> slidesServiceImpl.updateSlide("1",slidesDTO));
            verify(slidesRepository, never()).save(Mockito.any());
        }
    }
    @Nested
    class deleteSlides{
        @Test
        @DisplayName("News delete status ok")
        void deleteSlide() throws NotFoundException {
            Slides slides= SlidesServiceImplTest.generateSlidesLis().get(0);
            given(slidesRepository.findById(slides.getId())).willReturn(Optional.of(slides));
            willDoNothing().given(slidesRepository).deleteById(slides.getId());
            slidesServiceImpl.deleteSlide(slides.getId());
            verify(slidesRepository, Mockito.times(1)).deleteById(slides.getId());
        }
        @Test
        @DisplayName("News delete status Not Found")
        void deleteSlide_404(){
            Slides slides= SlidesServiceImplTest.generateSlidesLis().get(0);
            given(slidesRepository.findById(slides.getId())).willReturn(Optional.empty());
            assertThatThrownBy(()-> slidesServiceImpl.deleteSlide(slides.getId()))
                    .isInstanceOf(NotFoundException.class);
            verify(slidesRepository, never()).deleteById(slides.getId());
        }
    }
    @Nested
    class createSlides{
        @Test
        @DisplayName("Slides create status ok")
        void createSlide_Ok() throws IOException, NotFoundException {
            given(slidesRepository.getMaxOrder()).willReturn(1);
            given(organizationRepository.findById(any())).willReturn(Optional.of(generateOrganizations()));
            given(slidesRepository.save(any())).willReturn(generateSlidesLis().get(0));
            Slides slides= SlidesServiceImplTest.generateSlidesLis().get(0);
            SlidesDTO slidesDTO = slidesServiceImpl.createSlide(generateSlidesDto());
            assertThat(slides.getId()).isNotEmpty();
            assertThat(slides.getImageURL()).isEqualTo("image2");
            assertThat(slides.getOrder()).isEqualTo(1);
            verify(slidesRepository, times(1)).save(any());
        }
        @Test
        @DisplayName("Slides create status Not Found")
        void createSlide_404(){
            given(organizationRepository.findById(any())).willReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> slidesServiceImpl.createSlide(generateSlidesDto()));
            verify(slidesRepository, never()).findById(Mockito.any());
        }
        @Test
        @DisplayName("Slides create status Not Found")
        void createSlide_Null(){
            given(slidesRepository.getMaxOrder()).willReturn(null);
            assertThrows(NotFoundException.class, () -> slidesServiceImpl.createSlide(generateSlidesDto()));
            verify(slidesRepository, never()).save(any());
        }
    }

}
