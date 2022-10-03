package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.entity.Comment;
import com.alkemy.ong.mapper.CommentMapper;
import com.alkemy.ong.repository.CommentRepository;
import com.alkemy.ong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CommentMapper commentMapper;


    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> commentsEntityList = commentRepository.findAll();
        return commentMapper.commentsEntityList2DTOCommentsList(commentsEntityList);
    }
}