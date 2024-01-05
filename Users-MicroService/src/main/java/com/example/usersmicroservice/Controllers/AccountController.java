package com.example.usersmicroservice.Controllers;


import com.example.usersmicroservice.DTO.AccountCredentialsDTO;
import com.example.usersmicroservice.DTO.AccountDTO;
import com.example.usersmicroservice.Helpers.AccountHelper;
import com.example.usersmicroservice.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.usersmicroservice.Models.Account;

import java.util.List;
import java.util.Optional;

@RequestMapping("/account")
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthController authController;

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestBody AccountCredentialsDTO account) {
        // since I have a setter that hashes for password,
        // it's important for it to only be called once, and that's inside the registerUser() method.
        try {
            Account new_account = accountService.registerUser(new Account(
                    account.getId(), account.getFirst_name(),
                    account.getLast_name(), account.getEmail(),
                    account.getPassword(), null,
                    "USER"
            )); // because this route will only be used by normals user registering,
            // we will assign them the USER role by default.
            // TODO: Add a separate endpoint which the admin can use and can set specific the role
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Email already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return authController.loginAccount(account);
    }

    @GetMapping("/list")
    public List<Account> getAllUsers(Authentication authentication){
        System.out.println(authentication.getName());
        System.out.println(authentication.getAuthorities().toString());
        return accountService.getAllUsers();
    }
}
