package com.example.usersmicroservice.Services;

import com.example.usersmicroservice.DTO.AccountDTO;
import com.example.usersmicroservice.Models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    @Autowired
    private AccountService accountService;

    public TokenService(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        Optional<Account> user = accountService.getUserByEmail(authentication.getName());
        if (user.isPresent()) {
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plus(1, ChronoUnit.HOURS))
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .id(""+user.get().getId())
                    .build();
            return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        }else{
            return null;
        }

    }

    public AccountDTO validateToken(String token) {
        // We are relying on Spring Security to do the actual validating
        // By rules of authorities, if we get here we guarantee having a valid token.
        try {
            // Decode and validate the JWT token
            Jwt jwt = decoder.decode(token);
            System.out.println(jwt);
            String jti = jwt.getClaimAsString("jti");
            String email = jwt.getClaimAsString("sub");
            String role = jwt.getClaimAsString("scope");
            System.out.println("jti: " + jti);
            AccountDTO user = new AccountDTO(Long.parseLong(jti), null, null, email, role);

            return user; // Return the user details if the token is valid
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null; // Token is not valid
        }
    }

}
