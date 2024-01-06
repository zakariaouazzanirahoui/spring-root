package com.example.feedmicroservice.Controllers;

import com.example.feedmicroservice.Config.RequiredRole;
import com.example.feedmicroservice.DTO.CommentDTO;
import com.example.feedmicroservice.DTO.HelpPostWithUserDTO;
import com.example.feedmicroservice.DTO.VolunteeringPostWithUserDTO;
import com.example.feedmicroservice.Models.Comment;
import com.example.feedmicroservice.Models.HelpPost;
import com.example.feedmicroservice.Models.VolunteeringPost;
import com.example.feedmicroservice.Repositories.VolunteeringRepository;
import com.example.feedmicroservice.Services.CommentService;
import com.example.feedmicroservice.Services.HelpService;
import com.example.feedmicroservice.Services.PostService;
import com.example.feedmicroservice.Services.VolunteeringService;
import com.example.feedmicroservice.Threads.UserContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/volunteering")
public class VolunteeringController {
        private final VolunteeringService volunteeringService;
        private final PostService postService;
        private final CommentService commentService;
        private final VolunteeringRepository volunteeringRepository;


        @Autowired
        public VolunteeringController(VolunteeringService volunteeringService,PostService postService ,CommentService commentService,
                                      VolunteeringRepository volunteeringRepository) {
            this.volunteeringService = volunteeringService;
            this.postService = postService ;
            this.commentService = commentService;
            this.volunteeringRepository = volunteeringRepository;
        }


        @RequiredRole({"ADMIN","USER"})
        @GetMapping("/list")
        public List<VolunteeringPostWithUserDTO> getAllVolunteeringPosts() {
            return volunteeringService.getAllVolunteeringPosts();
        }



        @RequiredRole({"ADMIN","USER"})
        @PostMapping("/add")
        public ResponseEntity<VolunteeringPost> createHelp(@RequestParam("volunteeringPost") String volunteeringPostJson,
                                                           @RequestPart("file") MultipartFile file) throws JsonProcessingException  {
            ObjectMapper objectMapper = new ObjectMapper();
            VolunteeringPost volunteeringPost = objectMapper.readValue(volunteeringPostJson, VolunteeringPost.class);
            VolunteeringPost createdVolunteering = volunteeringService.createVolunteeringPost(volunteeringPost, file);
            return new ResponseEntity<>(createdVolunteering, HttpStatus.CREATED);
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
        @GetMapping("/getById/{volunteeringId}")
        public ResponseEntity<?> getVolunteeringPostById(@PathVariable Long volunteeringId) {
            try {
                VolunteeringPostWithUserDTO volunteeringPost = volunteeringService.getVolunteeringPostById(volunteeringId);
                return ResponseEntity.ok(volunteeringPost);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Volunteering post not found with id: " + volunteeringId);
            }
        }

        @RequiredRole({"ADMIN","USER"})
        @DeleteMapping ("/delete/{volunteeringId}")
        public ResponseEntity<?> deleteVolunteeringById(@PathVariable Long volunteeringId) {
            try {
                VolunteeringPostWithUserDTO volunteeringPost = volunteeringService.getVolunteeringPostById(volunteeringId);
                if (volunteeringPost.getAuthor().getId().equals(UserContextHolder.getUserInfo().getId())
                        || UserContextHolder.getUserInfo().getRole().equals("ADMIN")){
                    volunteeringService.deleteVolunteeringById(volunteeringId);
                }
                return ResponseEntity.ok("Volunteering post deleted successfully");
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Volunteering post not found with id: " + volunteeringId);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the Volunteering post");
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
        VolunteeringPost volunteeringPost = volunteeringService.findById(postId).orElse(null);;
        assert volunteeringPost != null;
        volunteeringPost.toggleIsStillNeeded();
        volunteeringRepository.save(volunteeringPost);
    }
    }


