package com.example.feedmicroservice.Controllers;


import com.example.feedmicroservice.Config.RequiredRole;
import com.example.feedmicroservice.DTO.GuidelineDTO;
import com.example.feedmicroservice.DTO.NewsDTO;
import com.example.feedmicroservice.Models.GuidelinePost;
import com.example.feedmicroservice.Models.NewsPost;
import com.example.feedmicroservice.Repositories.NewsRepository;
import com.example.feedmicroservice.Services.NewsService;
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
@RequestMapping("/news")
public class NewsController {


    private final NewsService newsService;
    private final PostService postService;
    private final NewsRepository newsRepository;

    public NewsController(NewsService newsService, PostService postService, NewsRepository newsRepository) {
        this.newsService = newsService;
        this.postService = postService;
        this.newsRepository = newsRepository;
    }


    @RequiredRole({"ADMIN","USER"})
    @PostMapping("/add")
    public ResponseEntity<NewsPost> createNews(
            @RequestParam("newsPost") String newsPosttJson,
            @RequestPart("file") MultipartFile file) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NewsPost newsPost = objectMapper.readValue(newsPosttJson, NewsPost.class);
        NewsPost createdNews = newsService.createNewsPost(newsPost, file);
        return new ResponseEntity<>(createdNews, HttpStatus.CREATED);
    }

    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/getById/{newsId}")
    public ResponseEntity<?> getNewsPostById(@PathVariable Long newsId) {
        try {
            NewsDTO newsPost = newsService.getNewsPostById(newsId);
            return ResponseEntity.ok(newsPost);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Help post not found with id: " + newsId);
        }
    }

    @RequiredRole({"ADMIN","USER"})
    @DeleteMapping ("/delete/{newsId}")
    public ResponseEntity<?> deleteNewsById(@PathVariable Long newsId) {
        try {
            NewsDTO newsPost = newsService.getNewsPostById(newsId);
            if (newsPost.getAuthor().getId().equals(UserContextHolder.getUserInfo().getId())
                    || UserContextHolder.getUserInfo().getRole().equals("ADMIN")){
                newsService.deleteNewsById(newsId);
            }
            return ResponseEntity.ok("Help post deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Help post not found with id: " + newsId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the help post");
        }
    }

    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/list")
    public List<NewsDTO> getAllNewsPosts() {
        return newsService.getAllNewsPosts();
    }


}
