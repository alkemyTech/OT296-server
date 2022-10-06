package com.alkemy.ong.controller;

import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.service.CommentService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id,Authentication authentication) {
        if(commentService.exitsById(id)){
            return new ResponseEntity<>("comment not found",HttpStatus.NOT_FOUND);
        }
        try {
            commentService.deleteComment(id,authentication);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }

}