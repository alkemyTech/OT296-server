package com.alkemy.ong.service;

import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.dto.PagesDTO;
import org.springframework.stereotype.Service;
import javassist.NotFoundException;

@Service
public interface NewsService {

	public void createNews(NewsDTO newsDTO);

	public NewsDTO getNewsById(String id);

	public NewsDTO updateNews(NewsDTO newsDTO, String id);

	public void deleteNews(String id) throws NotFoundException;

	PagesDTO<NewsDTO> getAllNewsForPages(int page);

}