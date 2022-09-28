package com.alkemy.ong.service.implement;

import java.util.Optional;
import com.alkemy.ong.dto.NewsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alkemy.ong.entity.News;
import com.alkemy.ong.mapper.NewsMapper;
import com.alkemy.ong.repository.NewsRepository;
import com.alkemy.ong.service.NewsService;
import javassist.NotFoundException;

@Service
public class NewsServiceImpl implements NewsService{
	
	@Autowired
	private NewsRepository newsRepository;
	
	@Autowired
	private NewsMapper newsMapper;

	@Override
	public void createNews(NewsDTO newsDTO) {
		News newsEntity = newsMapper.newsDTO2Entity(newsDTO);
		newsRepository.save(newsEntity);
	}

	@Override
	public NewsDTO getNewsById(String id) {
		News news = newsRepository.findById(id).orElse(null);
		assert news != null;
		return newsMapper.newsEntity2DTO(news);
	}

	@Override
	public void deleteNews(String id) throws NotFoundException {
    	Optional<News> news = newsRepository.findById(id);
    	if (news.isPresent()) {
    		newsRepository.deleteById(id);
    	} else {
    		throw new NotFoundException("News not found");
    	}		
	}

}