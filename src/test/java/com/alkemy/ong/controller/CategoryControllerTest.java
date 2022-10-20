package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.entity.Category;
import com.alkemy.ong.security.service.impl.UserServiceImpl;
import com.alkemy.ong.security.util.JwTUtil;
import com.alkemy.ong.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.NotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    CategoryService categoryService;
    @MockBean
    UserServiceImpl userService;
    @MockBean
    JwTUtil jwTUtil;
    @Autowired
    ObjectMapper objectMapper;
    static List<Category> generateCategoryList() {
        Category category = new Category();
        category.setName("Category");
        category.setDescription("Description");
        category.setImage("Image");
        category.setTimestamps(LocalDate.now());
        return Collections.singletonList(category);
    }
    static List<CategoryBasicDTO> generateCategoryBasicDTOList() {
        CategoryBasicDTO categoryBasicDTO = new CategoryBasicDTO();
        categoryBasicDTO.setName("CategoryBasic");
        return Collections.singletonList(categoryBasicDTO);
    }
    static List<CategoryDTO> generateCategoryDTO() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("CategoryDTO");
        categoryDTO.setDescription("Description");
        categoryDTO.setImage("Image");
        categoryDTO.setTimestamps(LocalDate.now());
        return Collections.singletonList(categoryDTO);
    }

    @Nested
    class getCategoryTest {

        @Test
        @DisplayName("Test Get Category status .OK")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
        void test1() throws Exception {
            List<CategoryBasicDTO> categoriesDTO = CategoryControllerTest.generateCategoryBasicDTOList();
            when(categoryService.getCategory()).thenReturn(categoriesDTO);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                    .andExpect(status().isAccepted());
            Mockito.verify(categoryService, Mockito.times(1)).getCategory();
        }

        @Test
        @DisplayName("Test Get Category status .FORBIDDEN invalid user")
        void test2() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).getCategory();
        }

        @Test
        @DisplayName("Test Get Category status .FORBIDDEN invalid token")
        void test3() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).getCategory();
        }

        @Test
        @DisplayName("Test Get Category by Id status .OK")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
        void test4() throws Exception {
            String id = "123";
            CategoryDTO categoryDTO = CategoryControllerTest.generateCategoryDTO().get(0);
            when(categoryService.getCategoryById(Mockito.any())).thenReturn(categoryDTO);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories" + "/{id}", id))
                    .andExpect(status().isOk());
            Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(Mockito.any());
        }

        @Test
        @DisplayName("Test Get Category by Id status .NOT_FOUND")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
        void test5() throws Exception {
            String id = "123";
            when(categoryService.getCategoryById(Mockito.any())).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", id))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService, Mockito.times(1)).getCategoryById(Mockito.any());
        }

        @Test
        @DisplayName("Test Get Category by Id status .FORBIDDEN invalid user")
        void test6() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            String id = "123";

            mockMvc.perform(MockMvcRequestBuilders.get("/categories" + "/{id}", id))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).getCategoryById(Mockito.any());
        }

        @Test
        @DisplayName("Test Get Category by Id status .FORBIDDEN invalid token")
        void test7() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "123";

            mockMvc.perform(MockMvcRequestBuilders.get("/categories" + "/{id}", id))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).getCategoryById(Mockito.any());
        }

        @Test
        @DisplayName("Test Get Page Category status .OK")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
        void test8() throws Exception {
            List<CategoryDTO> categoriesDTO = CategoryControllerTest.generateCategoryDTO();
            Pageable pageRequest = PageRequest.of(0, 10);
            Page<Category> categoryPage = new PageImpl<>(CategoryControllerTest.generateCategoryList(), pageRequest, categoriesDTO.size());
            Page<CategoryDTO> responsePage = new PageImpl<>(categoriesDTO, pageRequest, categoryPage.getSize());
            PagesDTO<CategoryDTO> response = new PagesDTO<>(responsePage,"previousPage","NextPage");

            when(categoryService.getAllForPages(0)).thenReturn(response);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories").param("page", "0")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService).getAllForPages(0);
        }

        @Test
        @DisplayName("Test Get Page Category status .INTERNAL_SERVER_ERROR")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_USER")
        void test9() throws Exception {
            when(categoryService.getAllForPages(-1)).thenThrow(IllegalArgumentException.class);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories").param("page", "-1")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService).getAllForPages(-1);
        }

        @Test
        @DisplayName("Test Get Page Category status .FORBIDDEN invalid user")
        void test10() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories").param("page", "0")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService, Mockito.never()).getAllForPages(0);
        }

        @Test
        @DisplayName("Test Get Page Category status .FORBIDDEN invalid token")
        void test11() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);

            mockMvc.perform(MockMvcRequestBuilders.get("/categories").param("page", "0")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService, Mockito.never()).getAllForPages(0);
        }
    }

    @Nested
    class createCategoryTest {

        @Test
        @DisplayName("Test Post Category status .OK")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            CategoryDTO categoryDTO = CategoryControllerTest.generateCategoryDTO().get(0);
            when(categoryService.createCategory(Mockito.any())).thenReturn(categoryDTO);

            mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                            .content(objectMapper.writeValueAsString(categoryDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(categoryDTO)))
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService).createCategory(Mockito.any());
        }

        @Test
        @DisplayName("Test Post Category status .FORBIDDEN invalid user")
        void test2() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);

            mockMvc.perform(MockMvcRequestBuilders.post("/categories"))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService, Mockito.never()).createCategory(Mockito.any());
        }

        @Test
        @DisplayName("Test Post Category status .FORBIDDEN invalid token")
        void test3() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);

            mockMvc.perform(MockMvcRequestBuilders.post("/categories"))
                    .andExpect(status().isForbidden())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService, Mockito.never()).createCategory(Mockito.any());
        }

        @Test
        @DisplayName("Test Post Category status .INTERNAL_SERVER_ERROR")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
        void test4() throws Exception {
            when(categoryService.createCategory(Mockito.any())).thenThrow(InternalError.class);

            mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError())
                    .andDo(MockMvcResultHandlers.print());
            Mockito.verify(categoryService, Mockito.never()).createCategory(Mockito.any());
        }
    }

    @Nested
    class updateCategoryTest {

        @Test
        @DisplayName("Test Put Category status .OK")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            String id = "id123";
            CategoryDTO categoryDTO = CategoryControllerTest.generateCategoryDTO().get(0);
            when(categoryService.updateCategory(Mockito.any(), Mockito.any())).thenReturn(categoryDTO);

            mockMvc.perform(MockMvcRequestBuilders.put("/categories" + "/{id}", id)
                            .content(objectMapper.writeValueAsString(categoryDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            Mockito.verify(categoryService, Mockito.times(1)).updateCategory(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Test Put Category status .NOT_FOUND")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
        void test2() throws Exception {
            String id = "id123";
            CategoryDTO categoryDTO = CategoryControllerTest.generateCategoryDTO().get(0);
            when(categoryService.updateCategory(Mockito.any(), Mockito.any())).thenThrow(NotFoundException.class);

            mockMvc.perform(MockMvcRequestBuilders.put("/categories" + "/{id}", id)
                            .content(objectMapper.writeValueAsString(categoryDTO))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
            Mockito.verify(categoryService, Mockito.times(1)).updateCategory(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Test Put Category status .FORBIDDEN invalid user")
        void test3() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            String id = "id123";

            mockMvc.perform(MockMvcRequestBuilders.put("/categories" + "/{id}", id))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).updateCategory(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Test Put Category status .FORBIDDEN invalid token")
        void test4() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "id123";

            mockMvc.perform(MockMvcRequestBuilders.put("/categories" + "/{id}", id))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).updateCategory(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Test Put Category status .INTERNAL_SERVER_ERROR")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
        void test5() throws Exception {
            String id = "id123";
            when(categoryService.updateCategory(Mockito.any(), Mockito.any())).thenThrow(InternalError.class);

            mockMvc.perform(MockMvcRequestBuilders.put("/categories" + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
            Mockito.verify(categoryService, Mockito.never()).updateCategory(Mockito.any(), Mockito.any());
        }
    }

    @Nested
    class deleteCategoryTest {

        @Test
        @DisplayName("Test Delete Category status .OK")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
        void test1() throws Exception {
            String id = "id123";

            Mockito.doNothing().when(categoryService).deleteCategory(id);

            mockMvc.perform(MockMvcRequestBuilders.delete("/categories" + "/{id}", id))
                    .andExpect(status().isOk());
            Mockito.verify(categoryService, Mockito.times(1)).deleteCategory(Mockito.any());
        }

        @Test
        @DisplayName("Test Delete Category status .NOT_FOUND")
        @WithMockUser(username = "flaambroggio@admin.com", authorities = "ROLE_ADMIN")
        void test2() throws Exception {
            String id = "id123";
            Mockito.doThrow(NotFoundException.class).when(categoryService).deleteCategory(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders.delete("/categories" + "/{id}", id))
                    .andExpect(status().isNotFound());
            Mockito.verify(categoryService, Mockito.times(1)).deleteCategory(Mockito.any());
        }

        @Test
        @DisplayName("Test Delete Category status .FORBIDDEN invalid user")
        void test3() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(true);
            when(jwTUtil.validateToken(Mockito.any(),Mockito.any())).thenReturn(false);
            String id = "id123";

            mockMvc.perform(MockMvcRequestBuilders.delete("/categories" + "/{id}", id))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).deleteCategory(Mockito.any());
        }

        @Test
        @DisplayName("Test Delete Category status .FORBIDDEN invalid token")
        void test4() throws Exception {
            when(jwTUtil.isBearer(Mockito.any())).thenReturn(false);
            String id = "id123";

            mockMvc.perform(MockMvcRequestBuilders.delete("/categories" + "/{id}", id))
                    .andExpect(status().isForbidden());
            Mockito.verify(categoryService, Mockito.never()).deleteCategory(Mockito.any());
        }
    }
}