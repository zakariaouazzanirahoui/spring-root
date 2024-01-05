package com.example.feedmicroservice.Repositories;

import com.example.feedmicroservice.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Additional custom queries if needed
}
