package com.alkemy.ong.controller;

import com.alkemy.ong.dto.NewsDTO;

import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.implement.NewsServiceImpl;
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

import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(NewsController.class)
class NewsControllerTest {
    @MockBean
    UserServiceImpl userService;
    @MockBean
    JwTUtil jwTUtil;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    NewsServiceImpl newsService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setting() {
        this.objectMapper = new ObjectMapper();
    }

    static NewsDTO generateNewsDto() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setName("name");
        newsDTO.setImage("image");
        newsDTO.setContent("content");
        return newsDTO;
    }

    static List<NewsDTO> generateDTOList() {
        NewsDTO dto = new NewsDTO();
        dto.setName("Testimonial DTO tests");
        dto.setContent("Hi!");
        dto.setImage("image.jpg");
        return Collections.singletonList(dto);
    }

    @Nested
    class createNewsTest {
        @Test
        @DisplayName("Valid create News")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void createNews_Ok() throws Exception {
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.createNews(Mockito.any(NewsDTO.class))).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService).createNews(Mockito.any());
        }

        @Test
        @DisplayName("Invalid role")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_USER")
        void createNew_403Role() throws Exception {
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.createNews(Mockito.any(NewsDTO.class))).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService, Mockito.never()).createNews(Mockito.any());
        }

        @Test
        @DisplayName("FORBIDDEN_invalid token")
        void createNews_403token() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.createNews(Mockito.any(NewsDTO.class))).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).createNews(Mockito.any());
        }

        @Test
        @DisplayName("FORBIDDEN_invalid user")
        void createNews_403User() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.createNews(Mockito.any(NewsDTO.class))).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).createNews(Mockito.any());
        }

        @Test
        @DisplayName("News server error")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void createNews_500() throws Exception {
            when(newsService.createNews(Mockito.any())).thenThrow(new InternalError());
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError());
            Mockito.verify(newsService, Mockito.never()).createNews(Mockito.any());
        }

        @Test
        @DisplayName("News Not Fount")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void createNews_404() throws Exception {
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.createNews(Mockito.any(NewsDTO.class))).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.post("/new")
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
            Mockito.verify(newsService, Mockito.never()).createNews(Mockito.any());
        }

        @Test
        @DisplayName("News Request_Image empty")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void createNews_Image400() throws Exception {
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            newsDTO.setName("");
            when(newsService.createNews(Mockito.any(NewsDTO.class))).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
            Mockito.verify(newsService, Mockito.never()).createNews(Mockito.any());
        }

        @Test
        @DisplayName("News Request_Image null")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void createNews_ImageNull400() throws Exception {
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            newsDTO.setName(null);
            when(newsService.createNews(Mockito.any(NewsDTO.class))).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.post("/news")
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
            Mockito.verify(newsService, Mockito.never()).createNews(Mockito.any());
        }
    }

    @Nested
    class updateNewsTest {
        @Test
        @DisplayName("Valid update News")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void updateNews_Ok() throws Exception {
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.updateNews(Mockito.any(), Mockito.any())).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", id)
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isAccepted())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService).updateNews(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Update News_Non existing id")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void updateNews_404() throws Exception {
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.updateNews(Mockito.any(), Mockito.any())).thenThrow(new EntityNotFoundException());
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", id)
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService).updateNews(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Invalid role")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_USER")
        void updateNews_403Role() throws Exception {
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.updateNews(Mockito.any(), Mockito.any())).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", id)
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService, Mockito.never()).updateNews(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("FORBIDDEN_invalid token")
        void updateNews_403Token() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.updateNews(Mockito.any(), Mockito.any())).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).updateNews(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("FORBIDDEN_invalid User")
        void updateNews_403User() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.updateNews(Mockito.any(), Mockito.any())).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).updateNews(Mockito.any(), Mockito.any());
        }
    }

    @Nested
    class getNewsTest {
        @Test
        @DisplayName("Valid get News")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void getNewsById_Ok() throws Exception {
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.getNewsById(id)).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", id)
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isAccepted())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService).getNewsById(Mockito.any());
        }

        @Test
        @DisplayName("FORBIDDEN_invalid token")
        void getNewsById_403token() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            String id = "1234id";
            when(newsService.getNewsById(id)).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).getNewsById(Mockito.any());
        }

        @Test
        @DisplayName("FORBIDDEN_invalid User")
        void getNewsById_403User() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.getNewsById(id)).thenReturn(newsDTO);
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).getNewsById(Mockito.any());
        }

        @Test
        @DisplayName("News server error")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void getNewsById_500() throws Exception {
            String id = "1234id";
            when(newsService.getNewsById(id)).thenThrow(new InternalError());
            mockMvc.perform(MockMvcRequestBuilders.put("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError());
            Mockito.verify(newsService, Mockito.never()).getNewsById(Mockito.any());
        }

        @Test
        @DisplayName("News Not Fount")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void getNewsById_404() throws Exception {
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.getNewsById(Mockito.any())).thenThrow(NotFoundException.class);
            mockMvc.perform(MockMvcRequestBuilders.get("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
            Mockito.verify(newsService).getNewsById(Mockito.any());
        }
    }

    @Nested
    class deleteNewsTest {
        @Test
        @DisplayName("Valid delete News")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void deleteNews_Ok() throws Exception {
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.deleteNews(id)).thenReturn("delete news");
            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", id)
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService).deleteNews(id);
        }

        @Test
        @DisplayName("News Not Found")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void deleteNews_NotFound() throws Exception {
            String id = "1234id";
            when(newsService.deleteNews(id)).thenThrow(new NotFoundException("Not Found"));
            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", id))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService).deleteNews(id);
        }

        @Test
        @DisplayName("Invalid role")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_USER")
        void deleteNews_403Role() throws Exception {
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.deleteNews(id)).thenReturn("delete news");
            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", id)
                            .content(objectMapper.writeValueAsString(newsDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService, Mockito.never()).deleteNews(id);
        }

        @Test
        @DisplayName("FORBIDDEN_invalid token")
        void deleteNews_403token() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.deleteNews(id)).thenReturn("delete news");
            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).deleteNews(id);
        }

        @Test
        @DisplayName("FORBIDDEN_invalid user")
        void deleteNews_403User() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "1234id";
            NewsDTO newsDTO = NewsControllerTest.generateNewsDto();
            when(newsService.deleteNews(id)).thenReturn("delete news");
            mockMvc.perform(MockMvcRequestBuilders.delete("/news/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).deleteNews(id);
        }
    }

    @Nested
    class getPageNewsTest {
        @Test
        @DisplayName("Valid case getPage News")
        @WithMockUser(username = "gocaroline@gmail.com", authorities = "ROLE_USER")
        void getNewsForPage_Ok() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
            List<NewsDTO> newsDTOS = NewsControllerTest.generateDTOList();
            Page<NewsDTO> page = new PageImpl<>(newsDTOS, pageable, newsDTOS.size());
            PagesDTO<NewsDTO> dtoPagesDTO = new PagesDTO<>(page, "previousPage", "nextPage");
            when(newsService.getAllNewsForPages(0)).thenReturn(dtoPagesDTO);
            mockMvc.perform(MockMvcRequestBuilders.get("/news").param("page", "0")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(newsService).getAllNewsForPages(0);
        }

        @Test
        @DisplayName("FORBIDDEN_invalid token")
        void getNewsForPage_403token() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(false);
            Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
            List<NewsDTO> newsDTOS = NewsControllerTest.generateDTOList();
            Page<NewsDTO> page = new PageImpl<>(newsDTOS, pageable, newsDTOS.size());
            PagesDTO<NewsDTO> dtoPagesDTO = new PagesDTO<>(page, "previousPage", "nextPage");
            when(newsService.getAllNewsForPages(0)).thenReturn(dtoPagesDTO);
            mockMvc.perform(MockMvcRequestBuilders.get("/news").param("page", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).getAllNewsForPages(0);
        }

        @Test
        @DisplayName("FORBIDDEN_invalid User")
        void getNewsForPage_403User() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
            List<NewsDTO> newsDTOS = NewsControllerTest.generateDTOList();
            Page<NewsDTO> page = new PageImpl<>(newsDTOS, pageable, newsDTOS.size());
            PagesDTO<NewsDTO> dtoPagesDTO = new PagesDTO<>(page, "previousPage", "nextPage");
            when(newsService.getAllNewsForPages(0)).thenReturn(dtoPagesDTO);
            mockMvc.perform(MockMvcRequestBuilders.get("/news").param("page", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
            Mockito.verify(newsService, Mockito.never()).getAllNewsForPages(0);
        }

        @Test
        @DisplayName("News server error")
        @WithMockUser(username = "gocaroline@admin.com", authorities = "ROLE_ADMIN")
        void getNewsForPage_400() throws Exception {
            Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
            List<NewsDTO> newsDTOS = NewsControllerTest.generateDTOList();
            Page<NewsDTO> page = new PageImpl<>(newsDTOS, pageable, newsDTOS.size());
            PagesDTO<NewsDTO> dtoPagesDTO = new PagesDTO<>(page, "previousPage", "nextPage");
            when(newsService.getAllNewsForPages(0)).thenReturn(dtoPagesDTO);
            mockMvc.perform(MockMvcRequestBuilders.get("/news")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
            Mockito.verify(newsService, Mockito.never()).getAllNewsForPages(0);
        }
    }
}