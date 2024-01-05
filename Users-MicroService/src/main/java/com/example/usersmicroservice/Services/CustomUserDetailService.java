package com.example.usersmicroservice.Services;

import com.example.usersmicroservice.Models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> userOptional = accountService.getUserByEmail(username);

        org.springframework.security.core.userdetails.User.UserBuilder userBuilder;

        if (userOptional.isPresent()) {
            Account user = userOptional.get();
            userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
            userBuilder.password(user.getPassword());
            String[] roles = {user.getRole()};
            userBuilder.authorities(roles);
        }else{
            throw new UsernameNotFoundException("User does not exist");
        }
        return userBuilder.build();
    }
}
