package com.kdw.boardapi.domain.post.repository;

import java.util.Optional;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kdw.boardapi.domain.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByDeletedFalse(Pageable pageable);
    Optional<Post> findByIdAndDeletedFalse(Long id);
    
} 