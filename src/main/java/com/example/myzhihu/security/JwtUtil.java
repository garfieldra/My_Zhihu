package com.example.myzhihu.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long expiration = 3600_000; // 一小时

    public String generateToken(String username)
    {
        return Jwts.builder()
                .setSubject(username)   //token的主体
                .setIssuedAt(new Date())  //签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(key)    //签名密钥
                .build()
                .parseClaimsJws(token)    // 解析token
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token)
    {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }
        catch(JwtException | IllegalArgumentException e)
        {
            return false;
        }
    }
}
