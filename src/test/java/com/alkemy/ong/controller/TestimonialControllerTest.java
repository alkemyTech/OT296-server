package com.alkemy.ong.controller;

import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.dto.TestimonialDTO;
import com.alkemy.ong.repository.TestimonialRepository;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.implement.TestimonialServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TestimonialController.class)
class TestimonialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwTUtil jwTUtil;

    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private ObjectMapper jsonMapper;

    @MockBean
    private TestimonialServiceImpl testimonialService;

    @Mock
    private TestimonialRepository testimonialRepository;

    @BeforeEach
    public void setting(){
        this.jsonMapper = new ObjectMapper();
    }

    @Nested
    class getTest{

        @Test
        @DisplayName("Valid Case getPage")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
            List<TestimonialDTO> testimonialDTOS = generateListDTO();
            Page<TestimonialDTO> page = new PageImpl<>(testimonialDTOS,pageable,testimonialDTOS.size());
            PagesDTO<TestimonialDTO> dtoPage = new PagesDTO<>(page,"previousPage","nextPage");
            Mockito.when(testimonialService.getAllForPages(0)).thenReturn(dtoPage);
            mockMvc.perform(get("/testimonials").param("page","0").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
            Mockito.verify(testimonialService).getAllForPages(0);

        }

        @Test
        @DisplayName("Forbidden getPage")
        void test2() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
            List<TestimonialDTO> testimonialDTOS = generateListDTO();
            Page<TestimonialDTO> page = new PageImpl<>(testimonialDTOS,pageable,testimonialDTOS.size());
            PagesDTO<TestimonialDTO> dtoPage = new PagesDTO<>(page,"previousPage","nextPage");
            Mockito.when(testimonialService.getAllForPages(0)).thenReturn(dtoPage);
            mockMvc.perform(get("/testimonials").param("page","0"))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            Mockito.verify(testimonialService,Mockito.never()).getAllForPages(0);

        }

        @Test
        @DisplayName("Invalid Token getPage")
        void test3() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
            List<TestimonialDTO> testimonialDTOS = generateListDTO();
            Page<TestimonialDTO> page = new PageImpl<>(testimonialDTOS,pageable,testimonialDTOS.size());
            PagesDTO<TestimonialDTO> dtoPage = new PagesDTO<>(page,"previousPage","nextPage");
            Mockito.when(testimonialService.getAllForPages(0)).thenReturn(dtoPage);
            mockMvc.perform(get("/testimonials").param("page","0"))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            Mockito.verify(testimonialService,Mockito.never()).getAllForPages(0);

        }

    }


    @Nested
    class postTest{

        @Test
        @DisplayName("Valid Case post")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            TestimonialDTO testimonialDTO = generateListDTO().get(0);
            Mockito.when(testimonialService.createTestimonial(Mockito.any())).thenReturn(testimonialDTO);
            mockMvc.perform(post("/testimonials")
                            .content(jsonMapper.writeValueAsString(testimonialDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(jsonMapper.writeValueAsString(testimonialDTO)))
                    .andDo(print());

            Mockito.verify(testimonialService).createTestimonial(Mockito.any());
        }

        @Test
        @DisplayName("Invalid Role post")
        @WithMockUser(username = "user@gmail.com",authorities = "ROLE_USER")
        void test2() throws Exception {
            TestimonialDTO testimonialDTO = generateListDTO().get(0);
            Mockito.when(testimonialService.createTestimonial(Mockito.any())).thenReturn(testimonialDTO);
            mockMvc.perform(post("/testimonials")
                            .content(jsonMapper.writeValueAsString(testimonialDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());

            Mockito.verify(testimonialService,Mockito.never()).createTestimonial(Mockito.any());
        }

        @Test
        @DisplayName("Forbidden post")
        void test3() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            TestimonialDTO testimonialDTO = generateListDTO().get(0);
            Mockito.when(testimonialService.createTestimonial(Mockito.any())).thenReturn(testimonialDTO);
            mockMvc.perform(post("/testimonials")
                            .content(jsonMapper.writeValueAsString(testimonialDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());

            Mockito.verify(testimonialService,Mockito.never()).createTestimonial(Mockito.any());
        }

        @Test
        @DisplayName("Invalid Token post")
        void test4() throws Exception {
            Mockito.when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            Mockito.when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            TestimonialDTO testimonialDTO = generateListDTO().get(0);
            Mockito.when(testimonialService.createTestimonial(Mockito.any())).thenReturn(testimonialDTO);
            mockMvc.perform(post("/testimonials")
                            .content(jsonMapper.writeValueAsString(testimonialDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());

            Mockito.verify(testimonialService,Mockito.never()).createTestimonial(Mockito.any());
        }

        @Test
        @DisplayName("Bad Request post")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test5() throws Exception {
            TestimonialDTO testimonialDTO = generateRequestWithBrokenAttributes();
            Mockito.when(testimonialService.createTestimonial(Mockito.any())).thenReturn(testimonialDTO);
            mockMvc.perform(post("/testimonials")
                            .content(jsonMapper.writeValueAsString(testimonialDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());

            Mockito.verify(testimonialService,Mockito.never()).createTestimonial(Mockito.any());
        }

        @Test
        @DisplayName("Server Error post")
        @WithMockUser(username = "admin@gmail.com",authorities = "ROLE_ADMIN")
        void test6() throws Exception {
            List<TestimonialDTO> testimonialDTO = generateListDTO();
            Mockito.when(testimonialService.createTestimonial(Mockito.any())).thenThrow(InternalError.class);
            mockMvc.perform(post("/testimonials")
                            .content(jsonMapper.writeValueAsString(testimonialDTO))
                            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andDo(print());

            Mockito.verify(testimonialService,Mockito.never()).createTestimonial(Mockito.any());
        }

    }

    @Nested
    class Update{ // status: 202, 400, 404, 500

        @Test
        @DisplayName("ADMIN: Update testimonial status.Accepted")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void Test1() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            given(testimonialService.updateTestimonial(any(), any())).willReturn(testimonialDTO);

            ResultActions result = mockMvc.perform(put("/testimonials/{id}", "1", testimonialDTO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));

            result.andExpect(status().isAccepted())
                    .andDo(print());
        }

        @Test
        @DisplayName("USER: Update testimonial status.Accepted")
        @WithMockUser(username = "usermock@gmail.com", roles = "USER")
        void Test2() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            given(testimonialService.updateTestimonial(any(), any())).willReturn(testimonialDTO);

            ResultActions result = mockMvc.perform(put("/testimonials/{id}", "1", testimonialDTO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));

            result.andExpect(status().isAccepted())
                    .andDo(print());
        }

        @Test
        @DisplayName("Update testimonial status.Bad Request")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void Test3() throws Exception {
            TestimonialDTO testimonialDTO = generateRequestWithBrokenAttributes();
            given(testimonialService.updateTestimonial(any(), any())).willReturn(testimonialDTO);

            ResultActions result = mockMvc.perform(put("/testimonials/{id}", "1", testimonialDTO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));

            result.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("Update testimonial status.Forbidden")
        void Test4() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            given(testimonialService.updateTestimonial(any(), any())).willThrow(NotFoundException.class);

            ResultActions result = mockMvc.perform(put("/testimonials/{id}", "1", testimonialDTO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));

            result.andExpect(status().isForbidden())
                    .andDo(print());
        }

        @Test
        @DisplayName("Update testimonial status.NotFound")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void Test5() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            given(testimonialService.updateTestimonial(any(), any())).willThrow(NotFoundException.class);

            ResultActions result = mockMvc.perform(put("/testimonials/{id}", "1", testimonialDTO)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));

            result.andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @DisplayName("Update testimonial status.Internal Server Error")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void Test6() throws Exception {
            List<TestimonialDTO> testimonialDTOList = generateListDTO();
            given(testimonialService.updateTestimonial(any(), any())).willThrow(NotFoundException.class);

            ResultActions result = mockMvc.perform(put("/testimonials/{id}", "1", testimonialDTOList)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTOList)));

            result.andExpect(status().isInternalServerError())
                    .andDo(print());
        }
    }

    @Nested
    class DeleteTestimonial{ // status: 202, 403, 404

        @Test
        @DisplayName("ADMIN: Delete testimonial status.OK")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void Test1() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            willDoNothing().given(testimonialService).deleteTestimonial(any());

            ResultActions result = mockMvc.perform(delete("/testimonials/{id}", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));
            result.andExpect(status().isOk())
                    .andDo(print());
            verify(testimonialService, times(1)).deleteTestimonial(any());
        }

        @Test
        @DisplayName("USER: Delete testimonial status.OK")
        @WithMockUser(username = "usermock@gmail.com", roles = "USER")
        void Test2() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            willDoNothing().given(testimonialService).deleteTestimonial(any());

            ResultActions result = mockMvc.perform(delete("/testimonials/{id}", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));
            result.andExpect(status().isOk())
                    .andDo(print());
            verify(testimonialService, times(1)).deleteTestimonial(any());
        }

        @Test
        @DisplayName("Delete testimonial status.Forbidden")
        void Test4() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            willDoNothing().given(testimonialService).deleteTestimonial(any());

            ResultActions result = mockMvc.perform(delete("/testimonials/{id}", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));
            result.andExpect(status().isForbidden())
                    .andDo(print());
            verify(testimonialService, never()).deleteTestimonial(any());
        }

        @Test
        @DisplayName("Delete testimonial status.NotFound")
        @WithMockUser(username = "adminmock@gmail.com", roles = "ADMIN")
        void Test5() throws Exception {
            TestimonialDTO testimonialDTO = generateTestimonialDTO();
            willThrow(NotFoundException.class).given(testimonialService).deleteTestimonial(any());

            ResultActions result = mockMvc.perform(delete("/testimonials/{id}", "1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonMapper.writeValueAsString(testimonialDTO)));
            result.andExpect(status().isNotFound())
                    .andDo(print());
            verify(testimonialService, times(1)).deleteTestimonial(any());
        }
    }

    private static TestimonialDTO generateTestimonialDTO(){
        TestimonialDTO testimonialDTO = new TestimonialDTO();
        testimonialDTO.setName("MyTestimonial");
        testimonialDTO.setImage("testimonial.jpg");
        testimonialDTO.setContent("This is a testimonial content, etc.");
        return testimonialDTO;
    }

    private static TestimonialDTO generateRequestWithBrokenAttributes(){
        TestimonialDTO request;

        // Blank Name
        request = generateTestimonialDTO();
        request.setName("");

        // Blank Content
        request = generateTestimonialDTO();
        request.setContent("");

        return request;
    }

    static List<TestimonialDTO> generateListDTO(){
        TestimonialDTO testimonialDTO1 = generateTestimonialDTO();
        return Collections.singletonList(testimonialDTO1);
    }

}