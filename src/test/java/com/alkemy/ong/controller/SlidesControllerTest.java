package com.alkemy.ong.controller;

import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.security.configuration.SecurityConfiguration;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.SlidesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SlidesController.class)
@Import({SecurityConfiguration.class, BCryptPasswordEncoder.class})
class SlidesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserServiceImpl userServiceImpl;

    @MockBean
    SlidesService slidesService;

    @MockBean
    JwTUtil jwTUtil;

    @Autowired
    ObjectMapper jsonMapper;

    @BeforeEach
    public void settings() {
        this.jsonMapper = new ObjectMapper();
    }

    static List<SlidesDTOPublic> generateListSlides(){
        SlidesDTOPublic slidesDTOPublic = new SlidesDTOPublic();
        slidesDTOPublic.setImageURL("image");
        slidesDTOPublic.setOrder(1);
        return Collections.singletonList(slidesDTOPublic);
    }

    static SlidesDTO generateSlidesDTO(){
        SlidesDTO slidesDTO = new SlidesDTO();
        slidesDTO.setText("text");
        slidesDTO.setOrder(1);
        slidesDTO.setImageURL("image");
        slidesDTO.setOrganizationID("1");
        return slidesDTO;
    }

    @Nested
    class getTest{

        @Test
        @DisplayName("Valid Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            List<SlidesDTOPublic> slides = generateListSlides();
            when(slidesService.getSlidesDTO()).thenReturn(slides);
            mvc.perform(MockMvcRequestBuilders.get("/Slides")
                            .content(jsonMapper.writeValueAsString(slides)))
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print());
            verify(slidesService).getSlidesDTO();
        }

        @Test
        @DisplayName("Forbidden Case")
        void test2() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            List<SlidesDTOPublic> slides = generateListSlides();
            when(slidesService.getSlidesDTO()).thenReturn(slides);
            mvc.perform(MockMvcRequestBuilders.get("/Slides"))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).getSlidesDTO();
        }

        @Test
        @DisplayName("Invalid Token Case")
        void test3() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            List<SlidesDTOPublic> slides = generateListSlides();
            when(slidesService.getSlidesDTO()).thenReturn(slides);
            mvc.perform(MockMvcRequestBuilders.get("/Slides"))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).getSlidesDTO();
        }

    }

    @Nested
    class getByIDTest{

        @Test
        @DisplayName("Valid Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.getSlideDTO(id)).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.get("/Slides/{id}",id))
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print());
            verify(slidesService).getSlideDTO(any());
        }

        @Test
        @DisplayName("NotFound Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test2() throws Exception {
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.getSlideDTO(id)).thenThrow(NotFoundException.class);
            mvc.perform(MockMvcRequestBuilders.get("/Slides/{id}",id)
                            .content(jsonMapper.writeValueAsString(slidesDTO)))
                    .andExpect(status().isNotFound())
                    .andDo(print());
            verify(slidesService).getSlideDTO(any());
        }

        @Test
        @DisplayName("Forbidden")
        void test3() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.getSlideDTO(id)).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.get("/Slides/{id}",id))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).getSlideDTO(any());
        }

        @Test
        @DisplayName("Invalid Token")
        void test4() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.getSlideDTO(id)).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.get("/Slides/{id}",id))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).getSlideDTO(any());
        }

    }

    @Nested
    class putTest{

        @Test
        @DisplayName("Valid Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.updateSlide(Mockito.any(), Mockito.any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.put("/Slides/{id}",id)
                    .content(jsonMapper.writeValueAsString(slidesDTO))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json(jsonMapper.writeValueAsString(slidesDTO)))
                    .andExpect(status().isAccepted())
                    .andDo(print());
            verify(slidesService).updateSlide(any(),any());
        }

        @Test
        @DisplayName("NotFound Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test2() throws Exception {
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.updateSlide(Mockito.any(), Mockito.any())).thenThrow(NotFoundException.class);
            mvc.perform(MockMvcRequestBuilders.put("/Slides/{id}",id)
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
            verify(slidesService).updateSlide(any(),any());
        }

        @Test
        @DisplayName("Role Invalid Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_USER")
        void test3() throws Exception {
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.updateSlide(Mockito.any(), Mockito.any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.put("/Slides/{id}",id)
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).updateSlide(any(),any());
        }

        @Test
        @DisplayName("Forbidden Case")
        void test4() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.updateSlide(Mockito.any(), Mockito.any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.put("/Slides/{id}",id)
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).updateSlide(any(),any());
        }

        @Test
        @DisplayName("Invalid Token Case")
        void test5() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            String id = "1";
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.updateSlide(Mockito.any(), Mockito.any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.put("/Slides/{id}",id)
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).updateSlide(any(),any());
        }

    }

    @Nested
    class deleteTest{

        @Test
        @DisplayName("Valid Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            String id = "1";
            doNothing().when(slidesService).deleteSlide(any());
            mvc.perform(MockMvcRequestBuilders.delete("/Slides/{id}",id))
                    .andExpect(status().isOk())
                    .andDo(print());
            verify(slidesService).deleteSlide(any());
        }

        @Test
        @DisplayName("NotFound Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test2() throws Exception {
            String id = "1";
            doThrow(NotFoundException.class).when(slidesService).deleteSlide(any());
            mvc.perform(MockMvcRequestBuilders.delete("/Slides/{id}",id))
                    .andExpect(status().isNotFound())
                    .andDo(print());
            verify(slidesService).deleteSlide(any());
        }

        @Test
        @DisplayName("Invalid Role Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_USER")
        void test3() throws Exception {
            String id = "1";
            doNothing().when(slidesService).deleteSlide(any());
            mvc.perform(MockMvcRequestBuilders.delete("/Slides/{id}",id))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).deleteSlide(any());
        }

        @Test
        @DisplayName("Forbidden Case")
        void test4() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "1";
            doNothing().when(slidesService).deleteSlide(any());
            mvc.perform(MockMvcRequestBuilders.delete("/Slides/{id}",id))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).deleteSlide(any());
        }

        @Test
        @DisplayName("Invalid Token Case")
        void test5() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            String id = "1";
            doNothing().when(slidesService).deleteSlide(any());
            mvc.perform(MockMvcRequestBuilders.delete("/Slides/{id}",id))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).deleteSlide(any());
        }


    }

    @Nested
    class postTest{

        @Test
        @DisplayName("Valid Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.createSlide(any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.post("/Slides")
                    .content(jsonMapper.writeValueAsString(slidesDTO))
                    .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
            verify(slidesService).createSlide(any());
        }

        @Test
        @DisplayName("NotFound Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test2() throws Exception {
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.createSlide(any())).thenThrow(NotFoundException.class);
            mvc.perform(MockMvcRequestBuilders.post("/Slides")
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
            verify(slidesService).createSlide(any());
        }



        @Test
        @DisplayName("Invalid Role Case")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_USER")
        void test3() throws Exception {
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.createSlide(any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.post("/Slides")
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).createSlide(any());
        }

        @Test
        @DisplayName("Forbidden Case")
        void test4() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.createSlide(any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.post("/Slides")
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).createSlide(any());
        }

        @Test
        @DisplayName("Invalid Token Case")
        void test5() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            SlidesDTO slidesDTO = generateSlidesDTO();
            when(slidesService.createSlide(any())).thenReturn(slidesDTO);
            mvc.perform(MockMvcRequestBuilders.post("/Slides")
                            .content(jsonMapper.writeValueAsString(slidesDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            verify(slidesService,never()).createSlide(any());
        }

    }

}