package com.example.feedmicroservice.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "news")
public class NewsPost extends Post {
    @ElementCollection
    private List<String> tags;
    //Media
    private String media;

}