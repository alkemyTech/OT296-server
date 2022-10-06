package com.alkemy.ong.service;

import com.alkemy.ong.dto.CommentDTOBody;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService{

    public List<CommentDTOBody> getAllComments();

    ResponseEntity<?> deleteComment(String id, Authentication authentication) throws NotFoundException;

    boolean exitsById(String id);

}