package com.alkemy.ong.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.dto.PagesDTO;
import com.alkemy.ong.entity.News;
import com.alkemy.ong.mapper.NewsMapper;
import com.alkemy.ong.repository.NewsRepository;
import com.alkemy.ong.service.implement.NewsServiceImpl;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NewsServiceImpl.class})
public class NewsServiceTest {
    @MockBean
    private NewsRepository newsRepository;
    @SpyBean
    NewsMapper newsMapper;
    @InjectMocks
    private NewsServiceImpl newsServiceImpl;
    @BeforeEach
    void SetUp(){
        newsServiceImpl = new NewsServiceImpl(newsRepository, newsMapper);
    }
    static NewsDTO generateNewsDto() {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setName("name");
        newsDTO.setImage("image");
        newsDTO.setContent("content");
        return newsDTO;
    }
    static News generateNews() {
        News news = new News();
        news.setId("1234jo");
        news.setName("name");
        news.setImage("image");
        news.setContent("content");
        return news;
    }
    @Nested
    class createNewsTest{
        @Test
        @DisplayName("News create status ok")
        void createNews_Ok(){
            NewsDTO newsDTO= NewsServiceTest.generateNewsDto();
            News news= newsMapper.newsDTO2Entity(newsDTO);
            given(newsRepository.save(news)).willReturn(news);
            given(newsMapper.newsDTO2Entity(newsDTO)).willReturn(news);
            given(newsMapper.newsEntity2DTO(news)).willReturn(newsDTO);
            newsServiceImpl.createNews(newsDTO);
            assertThat(newsDTO.getName()).isEqualTo(generateNewsDto().getName());
            verify(newsRepository, Mockito.times(1)).save(Mockito.any());
        }
        @Test
        @DisplayName("News create status Server error")
        void createNews_500(){
            NewsDTO newsDTO= NewsServiceTest.generateNewsDto();
            News news= newsMapper.newsDTO2Entity(newsDTO);
            given(newsMapper.newsDTO2Entity(newsDTO)).willReturn(news);
            given(newsRepository.save(news)).willThrow(InternalError.class);
            assertThrows(InternalError.class,() -> newsServiceImpl.createNews(newsDTO));
            verify(newsRepository, Mockito.times(1)).save(Mockito.any());
        }
    }
    @Nested
    class getNewsTest{
        @Test
        @DisplayName("News get status ok")
        void getNewsById_Ok() throws NotFoundException {
            News news=NewsServiceTest.generateNews();
            NewsDTO newsDTO= newsMapper.newsEntity2DTO(news);
            given(newsRepository.findById(news.getId())).willReturn(Optional.of(news));
            given(newsServiceImpl.getNewsById("1234jo")).willReturn(newsDTO);
            assertThat(news.getId()).isNotEmpty();
            verify(newsRepository, Mockito.times(1)).findById(Mockito.any());
        }
        @Test
        @DisplayName("News get AssertionError")
        void getNewsById_AssertionError() {
            News news=NewsServiceTest.generateNews();
            given(newsRepository.findById("1234jo")).willReturn(Optional.empty());
            assertThrows(AssertionError.class, () -> newsServiceImpl.getNewsById(news.getId()));
            verify(newsRepository, Mockito.times(1)).findById(Mockito.any());
        }
    }
    @Nested
    class updateNewsTest{
        @Test
        @DisplayName("News update status ok")
        void updateNews_Ok() throws NotFoundException {
            News news=NewsServiceTest.generateNews();
            NewsDTO newsDTO= newsMapper.newsEntity2DTO(news);
            given(newsRepository.save(news)).willReturn(news);
            given(newsRepository.findById("1234jo")).willReturn(Optional.of(news));
            newsServiceImpl.updateNews(newsDTO,"1234jo");
            assertThat(news.getName()).isEqualTo("name");
            verify(newsRepository, Mockito.times(1)).save(Mockito.any());
        }
    }
    @Nested
    class deleteNewsTest{
        @Test
        @DisplayName("News get status ok")
        void deleteNews_Ok() throws NotFoundException {
            News news=NewsServiceTest.generateNews();
            given(newsRepository.findById(news.getId())).willReturn(Optional.of(news));
            willDoNothing().given(newsRepository).deleteById(news.getId());
            newsServiceImpl.deleteNews(news.getId());
            verify(newsRepository, Mockito.times(1)).deleteById(news.getId());
        }
        @Test
        @DisplayName("News get status ok")
        void deleteNews_404() throws NotFoundException {
            News news=NewsServiceTest.generateNews();
            given(newsRepository.findById(news.getId())).willReturn(Optional.empty());
            assertThatThrownBy(()-> newsServiceImpl.deleteNews(news.getId()))
                    .isInstanceOf(NotFoundException.class);
            verify(newsRepository,never()).deleteById(news.getId());
        }
    }
    @Nested
    class getPageNewsTest{
        @Test
        @DisplayName("get members page")
        void getAllNewsForPages() {
            ArrayList<News> news = new ArrayList<>();
            news.add(new News());
            news.add(new News());
            news.add(new News());
            //List<NewsDTO> newsDTO = new ArrayList<>();
            Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
            Page<News> page = new PageImpl<>(news,pageable,news.size());
            when(newsRepository.findAll(any(Pageable.class))).thenReturn(page);
            PagesDTO<NewsDTO> newsPagesDTO2 = newsServiceImpl.getAllNewsForPages(0);
            assertThat(newsPagesDTO2).isNotNull();
            assertThat(news.size()).isEqualTo(3);
            verify(newsMapper,times(1)).newsEntityPageDTOList(any());
        }
        @Test
        @DisplayName("get members page")
        void getAllNewsForPages_405() {
            ArrayList<News> news = new ArrayList<>();
            news.add(new News());
            news.add(new News());
            news.add(new News());
            Pageable pageable = PageRequest.of(0,10, Sort.unsorted());
            Page<News> page = new PageImpl<>(news,pageable,news.size());
            given(newsRepository.findAll(any(Pageable.class))).willThrow(IllegalArgumentException.class);
            assertThrows(IllegalArgumentException.class, () -> newsServiceImpl.getAllNewsForPages(-1));
            verify(newsMapper,never()).newsEntityPageDTOList(any());
        }
    }
}
