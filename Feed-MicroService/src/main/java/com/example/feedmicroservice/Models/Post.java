package com.example.feedmicroservice.Models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Post {
    @Id @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String body;
    @JsonBackReference
    @Column(name = "user_id")
    private Long userId;

}

