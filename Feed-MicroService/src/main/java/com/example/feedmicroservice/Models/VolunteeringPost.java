package com.example.feedmicroservice.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "volunteering")
public class VolunteeringPost extends Post {

    @Column(name = "is_consumed")
    private boolean isConsumed;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public VolunteeringPost(){this.isConsumed=true; }

    private String media;


}