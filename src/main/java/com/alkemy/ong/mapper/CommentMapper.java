package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.CommentDTO;
import com.alkemy.ong.entity.Comment;

import java.util.ArrayList;
import java.util.List;

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

    public List<CommentDTO> commentsEntityList2DTOCommentsList(List<Comment> commentsEntityList) {
        List<CommentDTO> commentDTOList = new ArrayList<CommentDTO>();
        for(Comment comment : commentsEntityList){
            commentDTOList.add(this.commentEntity2CommentDTO(comment));
        }
        return commentDTOList;
    }
}