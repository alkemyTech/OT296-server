package com.alkemy.ong.controller;


import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.PagesDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alkemy.ong.dto.NewsDTO;
import com.alkemy.ong.service.NewsService;

import javassist.NotFoundException;

import javax.validation.Valid;

@RestController
@RequestMapping("/news")
@Tag(name = "News", description = "Create, update, show and delete News")
public class NewsController {
	
	@Autowired
	private NewsService newsService;
	@Tag(name = "News")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creation of a news")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Return created status code"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "New not found")
	})
	public ResponseEntity<NewsDTO> createNews(@Valid @RequestBody NewsDTO newsDTO) {
		newsService.createNews(newsDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	@Tag(name = "News")
	@PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Update News")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Update News"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "New not found")
	})
	public ResponseEntity<NewsDTO> updateNews(@RequestBody NewsDTO newsDTO, @PathVariable String id) {
		try {
			NewsDTO newsDTOUpdated = newsService.updateNews(newsDTO, id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(newsDTOUpdated);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	@Tag(name = "News")
	@GetMapping(value = "/{id}")
	@Operation(summary = "Get all News")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "202", description = "Found News"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "New not found")
	})
	public ResponseEntity<NewsDTO> getNewsById(@PathVariable String id){
		try {
			NewsDTO newsDTO = newsService.getNewsById(id);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(newsDTO);
		} catch (NotFoundException e) {
			return new ResponseEntity<NewsDTO>(HttpStatus.NOT_FOUND);
		}
	}
	@Tag(name = "News")
	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Delete News")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "News delete"),
			@ApiResponse(responseCode = "400", description = "Bad Request"),
			@ApiResponse(responseCode = "403", description = "Forbidden"),
			@ApiResponse(responseCode = "404", description = "New not found")
	})
	public ResponseEntity<String> deleteNews(@PathVariable String id) {
		try {
			newsService.deleteNews(id);
		} catch (NotFoundException e) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	@Tag(name = "News")
	@Operation(summary = "Get page of News")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the categories",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = NewsDTO.class))}),
			@ApiResponse(responseCode = "403", description = "User unauthorized",
					content = @Content),
	})
	@GetMapping(params = "page")
	public ResponseEntity<?> getNewsForPage(@RequestParam(defaultValue = "0") int page){
		PagesDTO<NewsDTO> pages = newsService.getAllNewsForPages(page);
		return ResponseEntity.status(HttpStatus.OK).body(pages);
	}

}