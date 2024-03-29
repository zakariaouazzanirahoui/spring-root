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
@Table(name = "guideline")
public class GuidelinePost extends Post {
    @ElementCollection
    private List<String> tags;
    //Media

    private String media;

}
