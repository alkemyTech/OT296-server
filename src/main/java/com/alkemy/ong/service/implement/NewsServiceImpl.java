package com.alkemy.ong.service.implement;

import java.util.Optional;
import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.dto.PagesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	public NewsDTO updateNews(NewsDTO newsDTO, String id) {
		News newsEntity = newsRepository.findById(id).orElse(null);
		assert newsEntity != null;
		newsEntity.setName(newsDTO.getName());
		newsEntity.setImage(newsDTO.getImage());
		newsEntity.setContent(newsDTO.getContent());
		News newsEntitySaved = newsRepository.save(newsEntity);
		return newsMapper.newsEntity2DTO(newsEntitySaved);
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

	@Override
	public PagesDTO<NewsDTO> getAllNewsForPages(int page) {
		if (page < 0) {
			throw new IllegalArgumentException("Page index must not be less than zero!");
		}
		Pageable pageRequest = PageRequest.of(page, 10);
		Page<News> news = newsRepository.findAll(pageRequest);
		return responsePage(news);
	}

	private PagesDTO<NewsDTO> responsePage(Page<News> page) {
		Page<NewsDTO> response = new PageImpl<>(
				newsMapper.newsEntityPageDTOList(page.getContent()),
				PageRequest.of(page.getNumber(), page.getSize()),
				page.getTotalElements());

		return new PagesDTO<>(response, "localhost:8080/news?page=");
	}

}