package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDTOBody>> getAllComments(){
        List<CommentDTOBody> commentDTOList = commentService.getAllComments();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commentDTOList);
    }
    @PostMapping
    public ResponseEntity<CommentDTO> Create (@Valid @RequestBody CommentDTO comment)  {
        try{
            ResponseEntity<CommentDTO> commentDTO = commentService.create(comment);
            return commentDTO;
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/posts/{id}/commets")
    public ResponseEntity<List<CommentDTOBody>> getAllPostComments(@PathVariable String id){
        List<CommentDTOBody> commentDTOList = commentService.getAllPostComments(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commentDTOList);
    }

}