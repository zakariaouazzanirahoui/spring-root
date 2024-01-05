package com.example.usersmicroservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountCredentialsDTO {
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String token;
    private String role;
}
