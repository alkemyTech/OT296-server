package com.alkemy.ong.controller;

import com.alkemy.ong.dto.SlidesDTOPublic;
import com.alkemy.ong.service.SlidesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
