package com.example.feedmicroservice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
@Setter
@Entity
@Table(name = "help")
public class HelpPost extends Post {
        @Column(name = "is_still_needed")
        private boolean isStillNeeded;
        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
        private List<Comment> comments = new ArrayList<>();

        private String media ;

        public HelpPost() {
        this.isStillNeeded = true;
    }
}
