package com.alkemy.ong.service;

import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@Service
public interface NewsService {
	
	public void deleteNews(String id) throws NotFoundException;

}
