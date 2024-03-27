package com.spring.demo.security.jwt;

import com.spring.demo.security.userprincal.UserPrinciple;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    private final String jwtSecret = "tienyeutranratlanhieulamluonnhieunhatthegioiluon";
    private final int jwtExpiration = 86400; //86400s = 1day

    public String createToken(Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(token);
//            Jwts.parser().verifyWith(jwtSecret).build().parseSignedClaims(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid jwt signature -> Message: {}", e);
        }
        catch (ExpiredJwtException e) {
            logger.error("Expired jwt token -> Message: {}", e);
        }
        return false;
    }

    public String getUserNameFromToken(String token) {
        String username = Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
//        String username = Jwts.parser().setSigningKey(jwtSecret).build().parseClaimsJws(token).getPayload().getSubject();
//        String username = Jwts.parser().setSigningKey(jwtSecret).build().parseSignedClaims(token).getPayload().getSubject();
        return username;
    }
}
