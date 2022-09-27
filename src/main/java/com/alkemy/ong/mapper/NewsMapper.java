package com.alkemy.ong.mapper;

import org.springframework.stereotype.Component;

import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.entity.News;

@Component
public class NewsMapper {
	
	public News newsDTO2Entity (NewsDTO newsDTO) {
		News news = new News();
		news.setContent(newsDTO.getContent());
		news.setCreateDateTime(newsDTO.getCreateDateTime());
		news.setId(newsDTO.getId());
		news.setImage(newsDTO.getImage());
		news.setName(newsDTO.getName());
		news.setUpdateDateTime(newsDTO.getUpdateDateTime());
		return news;
	}
	
	public NewsDTO newsEntity2DTO (News news) {
		NewsDTO newsDTO = new NewsDTO();
		newsDTO.setContent(news.getContent());
		newsDTO.setCreateDateTime(news.getCreateDateTime());
		newsDTO.setId(news.getId());
		newsDTO.setImage(news.getImage());
		newsDTO.setName(news.getName());
		newsDTO.setUpdateDateTime(news.getUpdateDateTime());
		return newsDTO; 
	}

}
