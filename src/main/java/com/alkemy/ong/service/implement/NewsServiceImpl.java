package com.alkemy.ong.service.implement;

import java.util.Optional;

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
	public void deleteNews(String id) throws NotFoundException {
    	Optional<News> news = newsRepository.findById(id);
    	if (news.isPresent()) {
    		newsRepository.deleteById(id);
    	} else {
    		throw new NotFoundException("News not found");
    	}		
	}

}
