package com.alkemy.ong.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.service.NewsService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/news")
public class NewsController {
	
	@Autowired
	private NewsService newsService;

	@GetMapping("{id}")
	public ResponseEntity<NewsDTO> getNewsById(@PathVariable String id) {
		try {
			NewsDTO newsDTO = newsService.getNewsById(id);
			return ResponseEntity.status(HttpStatus.FOUND).body(newsDTO);
		} catch (Exception e) {
			return new ResponseEntity("Organization not found", HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteNews(@PathVariable String id) {
		try {
			newsService.deleteNews(id);
		} catch (NotFoundException e) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
}
