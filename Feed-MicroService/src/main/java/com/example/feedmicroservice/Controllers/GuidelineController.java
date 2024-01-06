package com.example.feedmicroservice.Controllers;


import com.example.feedmicroservice.Config.RequiredRole;
import com.example.feedmicroservice.DTO.GuidelineDTO;
import com.example.feedmicroservice.DTO.HelpPostWithUserDTO;
import com.example.feedmicroservice.Models.GuidelinePost;
import com.example.feedmicroservice.Models.HelpPost;
import com.example.feedmicroservice.Repositories.GuidelineRepository;
import com.example.feedmicroservice.Services.GuidelineService;
import com.example.feedmicroservice.Services.PostService;
import com.example.feedmicroservice.Threads.UserContextHolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/guideline")
public class GuidelineController {

    private final GuidelineService guidelineService;
    private final PostService postService;
    private final GuidelineRepository guidelineRepository;

    public GuidelineController(GuidelineService guidelineService, PostService postService, GuidelineRepository guidelineRepository) {
        this.guidelineService = guidelineService;
        this.postService = postService;
        this.guidelineRepository = guidelineRepository;
    }

    @RequiredRole({"ADMIN","USER"})
    @PostMapping("/add")
    public ResponseEntity<GuidelinePost> createGuideline(
            @RequestParam("guidelinePost") String guidelinePostJson,
            @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GuidelinePost guidelinePost = objectMapper.readValue(guidelinePostJson, GuidelinePost.class);
        GuidelinePost createdGuideline = guidelineService.createGuidelinePost(guidelinePost, file);
        return new ResponseEntity<>(createdGuideline, HttpStatus.CREATED);
    }

    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/getById/{guidelineId}")
    public ResponseEntity<?> getGuidelinePostById(@PathVariable Long guidelineId) {
        try {
            GuidelineDTO guidelinePost = guidelineService.getGuidelinePostById(guidelineId);
            return ResponseEntity.ok(guidelinePost);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Help post not found with id: " + guidelineId);
        }
    }

    @RequiredRole({"ADMIN","USER"})
    @DeleteMapping ("/delete/{guidelineId}")
    public ResponseEntity<?> deleteGuidelineById(@PathVariable Long guidelineId) {
        try {
            GuidelineDTO guidelinePost = guidelineService.getGuidelinePostById(guidelineId);
            if (guidelinePost.getAuthor().getId().equals(UserContextHolder.getUserInfo().getId())
                    || UserContextHolder.getUserInfo().getRole().equals("ADMIN")){
                guidelineService.deleteGuidelineById(guidelineId);
            }
            return ResponseEntity.ok("Help post deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Help post not found with id: " + guidelineId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the help post");
        }
    }

    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/list")
    public List<GuidelineDTO> getAllHelpPosts() {
        return guidelineService.getAllGuidelinePosts();
    }



}
