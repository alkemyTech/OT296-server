package com.alkemy.ong.service;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService{

    public List<CommentDTOBody> getAllComments();
    ResponseEntity<CommentDTO> create(CommentDTO commentDTO) throws NotFoundException;

    public List<CommentDTOBody> getAllPostComments(String id);
}