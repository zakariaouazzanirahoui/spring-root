package com.example.feedmicroservice.Controllers;
import com.example.feedmicroservice.Config.RequiredRole;
import com.example.feedmicroservice.DTO.AnnouncementWithUserDTO;
import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.Models.AnnouncementPost;
import com.example.feedmicroservice.Services.AnnouncementService;
import com.example.feedmicroservice.Threads.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @RequiredRole({"ADMIN","USER"})
    @GetMapping("/list")
    public List<AnnouncementWithUserDTO> getAllAnnouncements() {
        return announcementService.getAllAnnouncementPosts();
    }


    @RequiredRole("ADMIN")
    @PostMapping("/add")
    public ResponseEntity<AnnouncementPost> createAnnouncement(@RequestBody AnnouncementPost announcement) {
        UserDTO userInfo = UserContextHolder.getUserInfo();
        announcement.setUserId(userInfo.getId());
        AnnouncementPost createdAnnouncement = announcementService.createAnnouncement(announcement);
        return new ResponseEntity<>(createdAnnouncement, HttpStatus.CREATED);
    }



}