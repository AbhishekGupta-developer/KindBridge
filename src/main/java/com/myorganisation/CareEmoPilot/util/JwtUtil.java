package com.myorganisation.CareEmoPilot.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String PURPOSE_CLAIM = "purpose";
    private static final String SIGNUP_PURPOSE = "signup";
    private static final String AUTH_PURPOSE = "auth";

    private final long SIGNUP_EXPIRATION = 1000L * 60 * 5;        // 5 minutes
    private final long AUTH_EXPIRATION = 1000L * 60 * 60 * 24;    // 24 hours

    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    //Signup JWT logic here
    public String generateSignupToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim(PURPOSE_CLAIM, SIGNUP_PURPOSE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + SIGNUP_EXPIRATION))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    private Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public boolean isValidSignupToken(String token) {
        try {
            Claims c = parse(token).getPayload();
            return SIGNUP_PURPOSE.equals(c.get(PURPOSE_CLAIM, String.class));
        } catch(JwtException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return parse(token).getPayload().getSubject();
    }

    //Signin logic here
    public String generateAuthToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim(PURPOSE_CLAIM, AUTH_PURPOSE)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + AUTH_EXPIRATION))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean isValidAuthToken(String token) {
        try {
            Claims c = parse(token).getPayload();
            return AUTH_PURPOSE.equals(c.get(PURPOSE_CLAIM, String.class));
        } catch (JwtException e) {
            return false;
        }
    }

}
