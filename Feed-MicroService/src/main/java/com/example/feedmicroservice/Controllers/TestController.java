package com.example.feedmicroservice.Controllers;

import com.example.feedmicroservice.Config.RequiredRole;
import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.Threads.UserContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/test")
    public String testEndpoint() {
        System.out.println("Controller: Test endpoint");

        return "Hello, World!";
    }
}
