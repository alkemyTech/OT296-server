package com.alkemy.ong.controller;

import com.alkemy.ong.dto.SlidesDTO;
import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.service.SlidesService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Slides")
public class SlidesController {

    @Autowired
    private SlidesService slidesService;

    @GetMapping()
    public ResponseEntity<List<SlidesDTOPublic>> getSlides () {
        return ResponseEntity.ok(slidesService.getSlidesDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSlide (@PathVariable String id) {
        try {
            slidesService.getSlideDTO(id);
        } catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSlide (@PathVariable String id, @RequestBody SlidesDTO slideDTO){
        try {
            slidesService.updateSlide(id, slideDTO);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSlide (@PathVariable String id) {
        try {
            slidesService.deleteSlide(id);
        }catch (NotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
