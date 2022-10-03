package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments(){
        List<CommentDTO> commentDTOList = commentService.getAllComments();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commentDTOList);
    }

}