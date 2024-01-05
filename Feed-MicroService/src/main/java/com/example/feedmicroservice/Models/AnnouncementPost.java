package com.example.feedmicroservice.Models;

import com.example.feedmicroservice.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@Setter
@Entity
@NoArgsConstructor
@Table(name = "announcement")

public class AnnouncementPost extends Post {
    // Announcement-specific attributes

    @JsonIgnore @Transient
    private transient UserDTO userDTO;


}
