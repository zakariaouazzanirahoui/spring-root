package com.example.feedmicroservice.DTO;


import com.example.feedmicroservice.Models.AnnouncementPost;
import com.example.feedmicroservice.Models.HelpPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelpPostWithUserDTO {
    private Long id;
    private String title;
    private String body;
    private boolean isStillNeeded;
    private UserDTO author;
    private List<CommentDTO> comments;
    private String media;
}
