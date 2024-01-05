package com.example.usersmicroservice.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;
    private String first_name;
    private String last_name;

    @Column(unique = true)
    private String email;

    private String password;

    private String salt;

    private String role; // ADMIN | EDITOR | USER

    public void setPassword(String password) {
        this.salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, this.salt);
    }
}
