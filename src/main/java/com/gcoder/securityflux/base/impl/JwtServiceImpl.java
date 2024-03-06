package com.gcoder.securityflux.base.impl;

import com.gcoder.securityflux.base.JwtService;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.security.Key;
import java.util.Date;
@Log4j2
public class JwtServiceImpl implements JwtService {

    private final long EXPIRATION_TIME;
    private Key KEY;

    public JwtServiceImpl(long exprirationTime) {
        EXPIRATION_TIME = exprirationTime;
        try {
            KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        } catch (Exception e) {
            log.error(e);
        }
        log.info("[Token test]: " + createJwt("admin"));
    }
    @Override
    public String parseToken(String credentials) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws((String)credentials).getBody().getSubject();
    }


    @Override
    public String createJwt(String subject) {
        JwtBuilder jwt = Jwts.builder()
                .setSubject(subject)
                .signWith(KEY);
        jwt.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        return jwt.compact();
    }

}