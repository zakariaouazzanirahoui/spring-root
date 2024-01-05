package com.example.feedmicroservice.Controllers;

import com.example.feedmicroservice.DTO.CommentDTO;
import com.example.feedmicroservice.Models.Comment;
import com.example.feedmicroservice.Models.Post;
import com.example.feedmicroservice.Services.CommentService;
import com.example.feedmicroservice.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService  ) {
        this.commentService = commentService;
    }

    /*
    @PostMapping("/add")
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }
     */

}
