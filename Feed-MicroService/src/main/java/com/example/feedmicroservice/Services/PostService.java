package com.example.feedmicroservice.Services;

import com.example.feedmicroservice.Models.Post;
import com.example.feedmicroservice.Repositories.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public void deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
        } else {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }
    }




}
