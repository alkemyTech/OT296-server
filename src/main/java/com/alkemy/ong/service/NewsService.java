package com.alkemy.ong.service;

import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.dto.PagesDTO;
import org.springframework.stereotype.Service;
import javassist.NotFoundException;

@Service
public interface NewsService {

	public NewsDTO createNews(NewsDTO newsDTO);

	public NewsDTO getNewsById(String id) throws NotFoundException;

	public NewsDTO updateNews(NewsDTO newsDTO, String id);

	public String deleteNews(String id) throws NotFoundException;

	PagesDTO<NewsDTO> getAllNewsForPages(int page);

}
