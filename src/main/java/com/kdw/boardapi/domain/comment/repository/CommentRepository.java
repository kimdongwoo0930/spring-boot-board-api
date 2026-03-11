package com.kdw.boardapi.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdw.boardapi.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByPostIdAndDeletedFalse(Long postId);
    Optional<Comment> findByIdAndDeletedFalse(Long id); 
}
