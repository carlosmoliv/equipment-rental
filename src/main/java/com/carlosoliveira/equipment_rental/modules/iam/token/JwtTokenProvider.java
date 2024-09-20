package com.carlosoliveira.equipment_rental.modules.iam.token;

import com.carlosoliveira.equipment_rental.modules.iam.ports.TokenService;
import com.carlosoliveira.equipment_rental.modules.user.domain.User;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenProvider implements TokenService {

    @Override
    public String generate(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigninKey())
                .compact();
    }

    private Key getSigninKey() {
        byte[] key = Decoders.BASE64.decode("414b1e7fe1ef832cd7196281557014f704bef678dbfa1c968129fcf97cb12555");
        return Keys.hmacShaKeyFor(key);
    }
}
