package com.volunnear.security.jwt;

import com.volunnear.dtos.CustomUserDetails;
import com.volunnear.services.users.UserService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.lifetime}")
    private Long jwtLifetime;
    private final UserService userService;

    public String createToken(CustomUserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        Date now = new Date();
        Date expireTime = new Date(now.getTime() + jwtLifetime);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return extractTokenFromHeader(bearerToken);
    }

    public String resolveToken(StompHeaderAccessor request) {
        String bearerToken = request.getFirstNativeHeader("Authorization");
        return extractTokenFromHeader(bearerToken);
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            log.info("Into validateToken");
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            log.info("Claims: {}", claims);
            boolean isValid = !claims.getBody().getExpiration().before(new Date());
            log.info("isValid: {}", isValid);
            return isValid;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        CustomUserDetails userDetails = userService.loadUserByUsername(getUsernameFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        return getClaimsFromToken(token).get("roles", List.class);
    }


    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
