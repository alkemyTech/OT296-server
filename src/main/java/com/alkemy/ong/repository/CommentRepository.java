package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query(value = "SELECT c FROM Comment c WHERE newsId = ?1")
    List<Comment> findAllByNewsId(String id);
}