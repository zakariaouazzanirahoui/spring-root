package com.example.feedmicroservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VolunteeringPostWithUserDTO {

    private Long id;
    private String title;
    private String body;
    private boolean isConsumed;
    private UserDTO author;
    private List<CommentDTO> comments;
    private String media;
}



