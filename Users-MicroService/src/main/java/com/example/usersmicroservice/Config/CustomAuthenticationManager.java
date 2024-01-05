package com.example.usersmicroservice.Config;

import com.example.usersmicroservice.Controllers.AuthController;
import com.example.usersmicroservice.Models.Account;
import com.example.usersmicroservice.Services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<Account> user = accountService.getUserByEmail(authentication.getName());
        if (user.isPresent()) {
            if (accountService.verifyPassword(authentication.getCredentials().toString(), user.get())) {
                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                grantedAuthorityList.add(new SimpleGrantedAuthority(user.get().getRole()));
                return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), grantedAuthorityList);
            }else {
                throw new BadCredentialsException("Wrong Password");
            }
        }else{
            throw new BadCredentialsException("Wrong Email");
        }
    }
}
