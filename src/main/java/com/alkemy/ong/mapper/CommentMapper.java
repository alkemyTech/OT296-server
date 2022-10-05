package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.entity.Comment;
import com.alkemy.ong.entity.News;
import com.alkemy.ong.entity.Users;
import com.alkemy.ong.repository.NewsRepository;
import com.alkemy.ong.repository.UsersRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public CommentDTO commentEntity2CommentDTO(Comment commentEntity){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBody(commentEntity.getBody());
        commentDTO.setNewsId(commentEntity.getNewsID());
        commentDTO.setUserId(commentEntity.getUserID());
        return commentDTO;
    }

    public Comment commentDTO2commentEntity (CommentDTO commentDTO) throws NotFoundException {
        Comment commentEntity = new Comment();
        commentEntity.setUserID(commentDTO.getUserId());
        commentEntity.setBody(commentDTO.getBody());
        commentEntity.setNewsID(commentDTO.getNewsId());
        return commentEntity;
    }

    public List<CommentDTOBody> commentsEntityList2DTOCommentsList(List<Comment> commentsEntityList) {
        List<CommentDTOBody> commentDTOList = new ArrayList<CommentDTOBody>();
        for(Comment comment : commentsEntityList){
            commentDTOList.add(this.commentEntity2CommentDTOBody(comment));
        }
        return commentDTOList;
    }

    private CommentDTOBody commentEntity2CommentDTOBody(Comment comment) {
        CommentDTOBody commentDTO = new CommentDTOBody();
        commentDTO.setBody(comment.getBody());
        return commentDTO;
    }
}