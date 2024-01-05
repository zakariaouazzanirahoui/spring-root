package com.example.feedmicroservice.DTO;

import com.example.feedmicroservice.Models.AnnouncementPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementWithUserDTO {
    private Long id;
    private String title;
    private String body;
    private UserDTO author;
}

