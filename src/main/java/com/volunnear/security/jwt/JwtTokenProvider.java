package com.volunnear.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.lifetime}")
    private Duration jwtLifetime;

    public String createToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        Date now = new Date();
        Date expireTime = new Date(now.getTime() + jwtLifetime.getNano());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsernameFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token){
        return getClaimsFromToken(token).get("roles", List.class);
    }


    private Claims getClaimsFromToken(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJwt(token)
                .getBody();
    }
}
