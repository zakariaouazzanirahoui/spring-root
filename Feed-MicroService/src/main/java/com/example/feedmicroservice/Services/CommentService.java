package com.example.feedmicroservice.Services;

import com.example.feedmicroservice.Models.Comment;
import com.example.feedmicroservice.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId){commentRepository.deleteById(commentId);}

    public Optional<Comment> getCommentById(Long commentId){
        return commentRepository.findById(commentId);
    }

    // Other service methods for Comment
}