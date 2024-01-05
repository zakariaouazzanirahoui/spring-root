package com.example.feedmicroservice.Controllers;

import com.example.feedmicroservice.Models.Post;
import com.example.feedmicroservice.Services.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PostController {
    @GetMapping("/public")
    public String publicEndpoint() {
        return "This is a public endpoint.";
    }

    @GetMapping("/secured")
    public String securedEndpoint() {
        return "This is a secured endpoint.";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "This is an admin endpoint.";
    }



}