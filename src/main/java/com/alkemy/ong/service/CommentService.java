package com.alkemy.ong.service;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService{

    public List<CommentDTOBody> getAllComments();
    ResponseEntity<CommentDTO> create(CommentDTO commentDTO) throws NotFoundException;

    ResponseEntity<?> updateComment(String id, CommentDTO commentDTO) throws NotFoundException;

    ResponseEntity<?> deleteComment(String id, Authentication authentication) throws NotFoundException;

    boolean exitsById(String id);

    public List<CommentDTOBody> getAllPostComments(String id);
}
