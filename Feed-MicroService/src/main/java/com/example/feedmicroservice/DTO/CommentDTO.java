package com.example.feedmicroservice.DTO;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String text;
    private UserDTO author;
    private Long postId;
    private Long userId;
}
