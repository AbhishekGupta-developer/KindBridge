package com.myorganisation.CareEmoPilot.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "zT9#Fn3@Xe7^Vr!qKpL2$Wu8Df0*GmYaJv1RxNcBZsQ";
    private final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 1000 * 60 * 5; //Valid for 5 mins

    //Signup JWT logic here
    public String generateSignupToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("purpose", "signup")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    private Jws<Claims> parse(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
    }

    public boolean isValidSignupToken(String token) {
        try {
            Claims c = parse(token).getBody();
            return "signup".equals(c.get("purpose", String.class));
        } catch(JwtException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return parse(token).getBody().getSubject();
    }

    //Signin logic here
    public String generateAuthToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("purpose", "auth")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME * 12 * 24)) // e.g., 24 hours if EXPIRATION_TIME is 5 min base
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidAuthToken(String token) {
        try {
            Claims c = parse(token).getBody();
            return "auth".equals(c.get("purpose", String.class));
        } catch (JwtException e) {
            return false;
        }
    }

}
