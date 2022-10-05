package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.dto.CommentDTOBody;
import com.alkemy.ong.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public CommentDTO commentEntity2CommentDTO(Comment commentEntity){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBody(commentEntity.getBody());
        commentDTO.setNews(commentEntity.getNews());
        commentDTO.setUser(commentEntity.getUser());
        return commentDTO;
    }

    public Comment commentDTO2commentEntity (CommentDTO commentDTO){
        Comment commentEntity = new Comment();
        commentEntity.setBody(commentDTO.getBody());
        commentEntity.setNews(commentDTO.getNews());
        commentEntity.setUser(commentDTO.getUser());
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