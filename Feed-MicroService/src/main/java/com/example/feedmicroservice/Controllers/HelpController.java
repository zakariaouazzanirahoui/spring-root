package com.example.feedmicroservice.Controllers;

import com.example.feedmicroservice.Config.RequiredRole;
import com.example.feedmicroservice.DTO.CommentDTO;
import com.example.feedmicroservice.DTO.HelpPostWithUserDTO;
import com.example.feedmicroservice.Models.AnnouncementPost;
import com.example.feedmicroservice.Models.Comment;
import com.example.feedmicroservice.Models.HelpPost;
import com.example.feedmicroservice.Models.Post;
import com.example.feedmicroservice.Repositories.HelpRepository;
import com.example.feedmicroservice.Services.CommentService;
import com.example.feedmicroservice.Services.HelpService;
import com.example.feedmicroservice.Services.PostService;
import com.example.feedmicroservice.Threads.UserContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/help")
public class HelpController {

    private final HelpService helpService;
    private final PostService postService;
    private final CommentService commentService;
    private final HelpRepository helpRepository;


    @Autowired
    public HelpController(HelpService helpService,PostService postService ,CommentService commentService,
                          HelpRepository helpRepository) {
        this.helpService = helpService;
        this.postService = postService ;
        this.commentService = commentService;
        this.helpRepository = helpRepository;
    }


    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/list")
    public List<HelpPostWithUserDTO> getAllHelpPosts() {
        return helpService.getAllHelpPosts();
    }

    @RequiredRole({"ADMIN","USER"})
    @PostMapping("/add")
    public ResponseEntity<HelpPost> createHelp(
            @RequestParam("helpPost") String helpPostJson,
            @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HelpPost helpPost = objectMapper.readValue(helpPostJson, HelpPost.class);
        HelpPost createdHelp = helpService.createHelpPost(helpPost, file);
        return new ResponseEntity<>(createdHelp, HttpStatus.CREATED);
    }

    @RequiredRole({"ADMIN","USER"})
    @PostMapping(value = "/addComment", consumes = {"application/json", "application/stream+json"})
    public ResponseEntity<Comment> addComment(@RequestBody CommentDTO commentDTO) {
        Long postId = commentDTO.getPostId();
        return postService.getPostById(postId).map(post -> {
            Comment comment = new Comment();
            comment.setText(commentDTO.getText());
            comment.setUserId(UserContextHolder.getUserInfo().getId());
            comment.setPost(post);
            Comment savedComment = commentService.createComment(comment);


            return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return 404 if post with postId is not found
    }


    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/getById/{helpId}")
    public ResponseEntity<?> getHelpPostById(@PathVariable Long helpId) {
        try {
            HelpPostWithUserDTO helpPost = helpService.getHelpPostById(helpId);
            return ResponseEntity.ok(helpPost);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Help post not found with id: " + helpId);
        }
    }

    @RequiredRole({"ADMIN","USER"})
    @DeleteMapping ("/delete/{helpId}")
    public ResponseEntity<?> deleteHelpById(@PathVariable Long helpId) {
        try {
            HelpPostWithUserDTO helpPost = helpService.getHelpPostById(helpId);
            if (helpPost.getAuthor().getId().equals(UserContextHolder.getUserInfo().getId())
                    || UserContextHolder.getUserInfo().getRole().equals("ADMIN")){
                helpService.deleteHelpById(helpId);
        }
            return ResponseEntity.ok("Help post deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Help post not found with id: " + helpId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the help post");
        }
    }

    @RequiredRole({"ADMIN","USER"})
    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long commentId){
        try {
            Optional<Comment> comment = commentService.getCommentById(commentId);
            if (comment.get().getUserId().equals(UserContextHolder.getUserInfo().getId())
                    || UserContextHolder.getUserInfo().getRole().equals("ADMIN")){
                commentService.deleteComment(commentId);
            }
            return ResponseEntity.ok("Comment deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment  not found with id: " + commentId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the comment ");
        }

    }
    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/changeStatus/{postId}")
    public void changePostStatus(@PathVariable Long postId){
        HelpPost helpPost = helpService.findById(postId).orElse(null);;
        assert helpPost != null;
        helpPost.toggleIsStillNeeded();
        helpRepository.save(helpPost);
    }


}
