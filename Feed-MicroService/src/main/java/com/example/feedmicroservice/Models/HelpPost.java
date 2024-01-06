package com.example.feedmicroservice.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
@Setter
@Entity
@NoArgsConstructor
@Table(name = "help")
public class HelpPost extends Post {

        @Column(name = "is_still_needed" ,columnDefinition = "VARCHAR(255) DEFAULT 'true'")
        private boolean isStillNeeded;
        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
        private List<Comment> comments = new ArrayList<>();

        private String media ;

        public void toggleIsStillNeeded() {
                this.isStillNeeded = !this.isStillNeeded;
        }




}
