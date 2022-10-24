package com.alkemy.ong.service.implement;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.entity.Comment;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.mapper.CommentMapper;
import com.alkemy.ong.repository.CommentRepository;
import com.alkemy.ong.repository.NewsRepository;
import com.alkemy.ong.repository.UsersRepository;
import com.alkemy.ong.service.CommentService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UsersRepository usersRepository;

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
    
    @Override
    public ResponseEntity<?> updateComment(String id, CommentDTO commentDTO) {
        Comment commentEntity = commentRepository.findById(id).orElse(null);
        assert commentEntity != null;
        Optional<Users> userEntity = usersRepository.findById(commentDTO.getUserId());
        if(commentEntity.getUser().getId().equals(commentDTO.getUserId()) || userEntity.get().getEmail().contains("admin")){
            commentEntity.setBody(commentDTO.getBody());
            commentRepository.save(commentEntity);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
      }
      
    // ---------------- Get Para Comentarios de un Post ----------------
    @Override
    public List<CommentDTOBody> getAllPostComments(String id) {
        List<Comment> commentsEntityList = commentRepository.findAllByNewsId(id);
        return commentMapper.commentsEntityList2DTOCommentsList(commentsEntityList);
    }


    @Override
    public ResponseEntity<?> deleteComment(String id, Authentication authentication) throws NotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        Optional<Users> users = usersRepository.findByEmail(authentication.getName());
        if(comment.get().getUser().getId().equals(users.get().getId()) || users.get().getEmail().contains("admin")) {
            commentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        else {
            throw new NotFoundException("you cant delete this comment");
        }
    }

    @Override
    public boolean exitsById(String id) {
        return commentRepository.findById(id).isEmpty();
    }
}