package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.entity.Comment;
import com.alkemy.ong.mapper.CommentMapper;
import com.alkemy.ong.repository.CommentRepository;
import com.alkemy.ong.repository.NewsRepository;
import com.alkemy.ong.repository.UsersRepository;
import com.alkemy.ong.service.CommentService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private NewsRepository newsRepository;

    @Override
    public List<CommentDTOBody> getAllComments() {
        List<Comment> commentsEntityList = commentRepository.findAll();
        return commentMapper.commentsEntityList2DTOCommentsList(commentsEntityList);
    }

    @Override
    public ResponseEntity<CommentDTO> create(CommentDTO commentDTO) throws NotFoundException {
        Comment comment=commentMapper.commentDTO2commentEntity(commentDTO);
        if (!usersRepository.existsById(commentDTO.getUserId())){
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        if (!newsRepository.existsById(commentDTO.getNewsId())){
            return new ResponseEntity("News not found", HttpStatus.NOT_FOUND);
        }
        Comment commentSave=commentRepository.save(comment);
        commentDTO=commentMapper.commentEntity2CommentDTO(commentSave);
        return new ResponseEntity("Comment created",HttpStatus.CREATED);
    }


}