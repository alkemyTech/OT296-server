package com.alkemy.ong.service;

import com.alkemy.ong.dto.NewsDTO;
import org.springframework.stereotype.Service;
import javassist.NotFoundException;

@Service
public interface NewsService {

	public void createNews(NewsDTO newsDTO);

	public NewsDTO getNewsById(String id);

	public void deleteNews(String id) throws NotFoundException;

}