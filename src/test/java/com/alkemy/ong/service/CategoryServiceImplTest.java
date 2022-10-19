package com.alkemy.ong.service;

import com.alkemy.ong.dto.CategoryBasicDTO;
import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.entity.Category;
import com.alkemy.ong.mapper.CategoryMapper;
import com.alkemy.ong.repository.CategoryRepository;
import com.alkemy.ong.service.implement.CategoryServiceImpl;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
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
@ContextConfiguration(classes = {CategoryServiceImpl.class})
public class CategoryServiceImplTest {

    @MockBean
    CategoryRepository categoryRepository;
    @SpyBean
    CategoryMapper categoryMapper;
    @InjectMocks
    CategoryServiceImpl categoryServiceImpl;
    @BeforeEach
    void SetUp(){
        categoryServiceImpl = new CategoryServiceImpl(categoryMapper,categoryRepository);
    }
    static CategoryDTO generateCategoryDto(){
        CategoryDTO categoryDTO= new CategoryDTO();
        categoryDTO.setName("name");
        categoryDTO.setDescription("ot296");
        categoryDTO.setImage("image.jpg");
        categoryDTO.setTimestamps(LocalDate.now());
        return categoryDTO;
    }
    static CategoryBasicDTO generateCategoryBasicDto(){
        CategoryBasicDTO categoryBasicDto= new CategoryBasicDTO();
        categoryBasicDto.setName("name");
        return categoryBasicDto;
    }
    static List<Category>generateCategoryList(){
        Category category= new Category();
        category.setId("1234jo");
        category.setName("caro");
        category.setDescription("Alkemy");
        category.setImage("image1");
        category.setTimestamps(LocalDate.now());
        return Collections.singletonList(category);
    }
    static Category generateCategory(){
        Category category= new Category();
        category.setId("2");
        category.setName("name");
        category.setDescription("ot296");
        category.setImage("image.jpg");
        category.setTimestamps(LocalDate.now());
        return category;
    }

    @Test
    @DisplayName("Test Get All Category status .OK")
    void getCategory_Ok() {
        List<Category> categories = CategoryServiceImplTest.generateCategoryList();
        given(categoryRepository.findAll()).willReturn(categories);
        List<CategoryBasicDTO> categoryBasicDTOS = categoryMapper.categoryEntityList2DTO(categories);
        given(categoryServiceImpl.getCategory()).willReturn(categoryBasicDTOS);
        assertThat(categoryBasicDTOS).isNotNull();
        assertThat(categoryBasicDTOS.size()).isEqualTo(1);
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }
    @Test
    @DisplayName("Test Get Id Category status .OK")
    void getCategoryById_Ok(){
        Category category = CategoryServiceImplTest.generateCategory();
        CategoryDTO categoryDTO= categoryMapper.categoryEntity2DTO(category);
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        given(categoryServiceImpl.getCategoryById("2")).willReturn(categoryDTO);
        assertThat(category.getId()).isNotEmpty();
        verify(categoryRepository, Mockito.times(1)).findById(Mockito.any());
    }
    @Test
    @DisplayName("News delete status ok")
    void deleteNews_Ok() throws NotFoundException {
        Category category = CategoryServiceImplTest.generateCategory();
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        willDoNothing().given(categoryRepository).deleteById(category.getId());
        categoryServiceImpl.deleteCategory(category.getId());
        verify(categoryRepository, Mockito.times(1)).deleteById(category.getId());
    }
    @Test
    @DisplayName("News delete status Not Found")
    void deleteNews_404() throws NotFoundException {
        Category category = CategoryServiceImplTest.generateCategory();
        given(categoryRepository.findById(category.getId())).willReturn(Optional.empty());
        assertThatThrownBy(()-> categoryServiceImpl.deleteCategory(category.getId()))
                .isInstanceOf(NotFoundException.class);
        verify(categoryRepository, never()).deleteById(category.getId());
    }
    @Test
    @DisplayName("News create status ok")
    void createCategory_Ok(){
        CategoryDTO categoryDTO= CategoryServiceImplTest.generateCategoryDto();
        Category category= categoryMapper.categoryDTO2Entity(categoryDTO);
        given(categoryRepository.save(category)).willReturn(category);
        given(categoryMapper.categoryDTO2Entity(categoryDTO)).willReturn(category);
        given(categoryMapper.categoryEntity2DTO(category)).willReturn(categoryDTO);
        categoryServiceImpl.createCategory(categoryDTO);
        assertThat(categoryDTO.getName()).isEqualTo(generateCategoryDto().getName());
        verify(categoryRepository, Mockito.times(1)).save(Mockito.any());
    }
    @Test
    @DisplayName("Get category Page")
    void getAllForPages() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());
        categories.add(new Category());
        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        Page<Category> page = new PageImpl<>(categories,pageable,categories.size());
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
        PagesDTO<CategoryDTO> categoryDTOPagesDTO = categoryServiceImpl.getAllForPages(0);
        assertThat(categoryDTOPagesDTO).isNotNull();
        assertThat(categories.size()).isEqualTo(3);
        verify(categoryMapper,times(1)).categoryEntityPageDTOList(any());
    }
    @Test
    @DisplayName("Get Category page-IllegalArgumentException")
    void getAllForPages_IllegalArgumentException() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category());
        categories.add(new Category());
        categories.add(new Category());
        Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
        Page<Category> page = new PageImpl<>(categories,pageable,categories.size());
        given(categoryRepository.findAll(any(Pageable.class))).willThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> categoryServiceImpl.getAllForPages(-1));
        verify(categoryMapper,never()).categoryEntityPageDTOList(any());
    }
    @Test
    @DisplayName("News update status ok")
    void updateCategory_Ok() throws NotFoundException {
        Category category = CategoryServiceImplTest.generateCategory();
        CategoryDTO categoryDTO= categoryMapper.categoryEntity2DTO(category);
        given(categoryRepository.findById("2")).willReturn(Optional.of(category));
        given(categoryServiceImpl.updateCategory(categoryDTO,"2")).willReturn(categoryDTO);
        assertThat(category.getId()).isNotEmpty();
        assertThat(category.getName()).isEqualTo("name");
        verify(categoryRepository, Mockito.times(1)).findById(Mockito.any());
    }
    @Test
    @DisplayName("News update status ok")
    void updateCategory_404() throws NotFoundException {
        Category category = CategoryServiceImplTest.generateCategory();
        CategoryDTO categoryDTO= categoryMapper.categoryEntity2DTO(category);
        given(categoryRepository.findById(Mockito.any())).willReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> categoryServiceImpl.updateCategory(categoryDTO,"2"));
        verify(categoryRepository, never()).save(Mockito.any());
    }


}
