package com.ufcg.booker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration.time.seconds}")
    private int timeToExpireInSeconds;

    public String generateToken(Authentication authentication) {
        UserDetails user = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiration = new Date(new Date().getTime() + timeToExpireInSeconds * 1000L);
        return Jwts.builder()
                   .setIssuer("Booker")
                   .setSubject(user.getUsername())
                   .setIssuedAt(now)
                   .setExpiration(expiration)
                   .signWith(SignatureAlgorithm.HS256, this.secret)
                   .compact();
    }

    public boolean isValid(String jwt) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(jwt);
            return true;
        } catch (ClaimJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        Assert.isTrue(isValid(jwt), "JWT is not valid #BUG");

        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }
}
