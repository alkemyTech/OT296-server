package com.alkemy.ong.service;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;

import java.util.List;

public interface CommentService{

    public List<CommentDTOBody> getAllComments();

}