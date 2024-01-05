package com.example.feedmicroservice.Models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "guideline")
public class GuidelinePost extends Post {
    @ElementCollection
    private List<String> tags;
    //Media

}
