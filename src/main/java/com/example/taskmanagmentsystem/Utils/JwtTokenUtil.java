package com.example.taskmanagmentsystem.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails user) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("username", user.getUsername());
        payload.put("roles", user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() + jwtLifetime.toMillis());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setClaims(payload)
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
//        System.out.println("Username: " + parseToken(token).get("username", String.class));
        return parseToken(token).get("username", String.class);
    }

    public List<String> getRoles(String token) {
//        parseToken(token).get("roles", List.class).stream().forEach(System.out::println);
        return parseToken(token).get("roles", List.class);
    }

    public Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
//            System.out.println("Parsed token claims: " + claims);
            return claims;
        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
            throw e;
        }
    }
}
