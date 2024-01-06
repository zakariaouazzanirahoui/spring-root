package com.example.feedmicroservice.DTO;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GuidelineDTO {
    private Long id;
    private String title;
    private String body;
    private UserDTO author;
    private List<String> tags;
    private String media;
}
