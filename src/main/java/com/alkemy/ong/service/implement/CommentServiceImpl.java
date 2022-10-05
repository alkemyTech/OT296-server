package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.entity.Comment;
import com.alkemy.ong.mapper.CommentMapper;
import com.alkemy.ong.repository.CommentRepository;
import com.alkemy.ong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;


    @Override
    public List<CommentDTOBody> getAllComments() {
        List<Comment> commentsEntityList = commentRepository.findAll();
        return commentMapper.commentsEntityList2DTOCommentsList(commentsEntityList);
    }
}