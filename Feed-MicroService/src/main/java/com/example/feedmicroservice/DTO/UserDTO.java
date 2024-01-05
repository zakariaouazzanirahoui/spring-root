package com.example.feedmicroservice.DTO;

import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String role;

}
