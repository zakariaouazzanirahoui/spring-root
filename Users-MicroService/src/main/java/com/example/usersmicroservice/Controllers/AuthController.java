package com.example.usersmicroservice.Controllers;

import com.example.usersmicroservice.DTO.AccountCredentialsDTO;
import com.example.usersmicroservice.DTO.AccountDTO;
import com.example.usersmicroservice.Helpers.AccountHelper;
import com.example.usersmicroservice.Models.Account;
import com.example.usersmicroservice.Services.AccountService;
import com.example.usersmicroservice.Services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthenticationManager authenticationManager;

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody AccountCredentialsDTO account) {
       try{
           String token = tokenService.generateToken(authenticate(account.getEmail(),account.getPassword()));
           Optional<Account> account_info = accountService.getUserByEmail(account.getEmail());
           if (account_info.isPresent()) {
               account.setFirst_name(account_info.get().getFirst_name());
               account.setLast_name(account_info.get().getLast_name());
               account.setRole(account_info.get().getRole());
               account.setId(account_info.get().getId());
           }
           account.setToken(token);
           account.setPassword(null);
           return new ResponseEntity<>(account, HttpStatus.OK);
       }catch(Exception e) {
           return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
       }
    }

    private Authentication authenticate(String userName, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (Exception e) {
            throw new Exception("Wrong UserName or Password");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        AccountDTO userInfo = new AccountDTO();
        Optional<Account> userOptional = accountService.getUserById(userId);
        if (userOptional.isPresent()) {
            userInfo.setFirst_name(userOptional.get().getFirst_name());
            userInfo.setLast_name(userOptional.get().getLast_name());
            userInfo.setRole(userOptional.get().getRole());
            userInfo.setId(userOptional.get().getId());
            userInfo.setEmail(userOptional.get().getEmail());
        }
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
    @PostMapping("/bulk")
    public ResponseEntity<Map<Long, AccountDTO>> getUsersByIds(@RequestParam Set<Long> userIds) {
        List<Account> users = accountService.getUsersByIds(userIds);
        Map<Long, AccountDTO> userDTOMap = users.stream()
                .collect(Collectors.toMap(Account::getId, user -> {
                    AccountDTO userInfo = new AccountDTO();
                    userInfo.setId(user.getId());
                    userInfo.setFirst_name(user.getFirst_name());
                    userInfo.setLast_name(user.getLast_name());
                    userInfo.setRole(user.getRole());
                    userInfo.setEmail(user.getEmail());
                    return userInfo;
                }));

        return new ResponseEntity<>(userDTOMap, HttpStatus.OK);
    }
    @GetMapping("/validate-token")
    public AccountDTO validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extract the token from the Authorization header
            String token = extractToken(authorizationHeader);

            // Validate the token
            AccountDTO user = tokenService.validateToken(token);
            return user;
        } catch (Exception e) {
            return null;
        }
    }
    private String extractToken(String authorizationHeader) {
        // Assuming the token is included in the "Bearer" scheme
        return authorizationHeader.replace("Bearer ", "");
    }
}

